<script setup>
import { DateFormatter, getLocalTimeZone } from '@internationalized/date'
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog/index.js'
import { Calendar } from '@/components/ui/calendar/index.js'
import { CalendarIcon, Plus } from 'lucide-vue-next'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select/index.js'
import { Label } from '@/components/ui/label/index.js'
import { Input } from '@/components/ui/input/index.js'
import { Button } from '@/components/ui/button/index.js'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover/index.js'
import { onMounted, ref, watch } from 'vue'

import {
  Combobox,
  ComboboxAnchor,
  ComboboxEmpty,
  ComboboxGroup,
  ComboboxInput,
  ComboboxItem,
  ComboboxItemIndicator,
  ComboboxList,
  ComboboxTrigger,
} from '@/components/ui/combobox'
import { Check, ChevronsUpDown, Search } from 'lucide-vue-next'

const props = defineProps({
  typeId: { type: Number, required: true },
})

const dateFormat = new DateFormatter('nb-NO', {
  dateStyle: 'long',
})

const expirationDate = ref()

const registeredItems = ref([
  {
    id: 1,
    name: 'Hermetisk kjøtt',
    type: 1,
    unit: 2,
  },
  {
    id: 2,
    name: 'Drikkevann',
    type: 1,
    unit: 3,
  },
  {
    id: 3,
    name: 'Tørrmelk',
    type: 1,
    unit: 2,
  },
  {
    id: 4,
    name: 'Knekkebrød',
    type: 1,
    unit: 1,
  },
  {
    id: 5,
    name: 'Lommelykt',
    type: 2,
    unit: 1,
  },
  {
    id: 6,
    name: 'Stearinlys',
    type: 2,
    unit: 1,
  },
  {
    id: 7,
    name: 'Batterier',
    type: 2,
    unit: 1,
  },
  {
    id: 8,
    name: 'Vedkubber',
    type: 2,
    unit: 1,
  },
  {
    id: 9,
    name: 'Nødradio',
    type: 3,
    unit: 1,
  },
  {
    id: 10,
    name: 'Brosjyre med nødinfo',
    type: 3,
    unit: 1,
  },
  {
    id: 11,
    name: 'Førstehjelpsskrin',
    type: 4,
    unit: 1,
  },
  {
    id: 12,
    name: 'Smertestillende tabletter',
    type: 4,
    unit: 1,
  },
  {
    id: 13,
    name: 'Såpe',
    type: 4,
    unit: 3,
  },
  {
    id: 14,
    name: 'Desinfeksjonsmiddel',
    type: 4,
    unit: 3,
  },
  {
    id: 15,
    name: 'Kontanter',
    type: 5,
    unit: 1,
  },
  {
    id: 16,
    name: 'Lighter',
    type: 5,
    unit: 1,
  },
])

const isNewItemDialogOpen = ref();

const newItemName = ref()
const itemAmount = ref()
const selectedItemId = ref()
const selectedItemName = ref()
const selectedItemType = ref()
const selectedItemUnit = ref()

const isCustomItemSelected = ref(false)
const onItemSelected = (item) => {
  console.log(item.name)
  if (item.id === -1) {
    isCustomItemSelected.value = true
    selectedItemUnit.value = ''
    selectedItemType.value = props.typeId
  } else {
    isCustomItemSelected.value = false
    selectedItemUnit.value = item.unit
    selectedItemType.value = item.type
  }
  selectedItemName.value = item.name
  selectedItemId.value = item.id
}

const refreshFields = () => {
  itemAmount.value = '';
  selectedItemUnit.value = '';
  selectedItemName.value = '';
  expirationDate.value = '';
}

const handleSubmit = () => {
  console.log('item id: ' + selectedItemId.value)
  console.log('item name: ' + selectedItemName.value)
  console.log('item type: ' + selectedItemType.value)
  console.log('item unit: ' + selectedItemUnit.value)
  console.log('item amount: ' + itemAmount.value)
  console.log('item date: ' + expirationDate.value)

  refreshFields();
  isNewItemDialogOpen.value = false;
}

const handleKeydown = (e) => {
  if (e.key === '-' || e.key === 'e' || e.key === '+') {
    e.preventDefault();
  }
};

watch(isNewItemDialogOpen, (value) => {
    if (!value) {
      refreshFields();
    }
  },
)

watch(
  () => props.typeId,
  () => {
    selectedItemType.value = props.typeId
  },
)

onMounted(() => {
  selectedItemType.value = props.typeId
})
</script>

<template>
    <Dialog v-model:open="isNewItemDialogOpen">
      <DialogTrigger>
        <Button type="button">Legg til</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Legg til ny vare</DialogTitle>
          <DialogDescription>
          </DialogDescription>
        </DialogHeader>
        <form @submit.prevent="handleSubmit" class="contents">

        <Label>Navn på vare</Label>
        <div class="flex gap-3 w-full">
          <Combobox class="w-full">
            <ComboboxAnchor class="w-full">
              <div class="relative w-full items-center">
                <ComboboxInput
                  required
                  v-model="newItemName"
                  class="pl-9 w-full"
                  :display-value="(item) => item?.name ?? ''"
                  placeholder="Skriv inn navn på vare..."
                />
                <span class="absolute start-0 inset-y-0 flex items-center justify-center px-3">
                  <Search class="size-4 text-muted-foreground" />
                </span>
              </div>
            </ComboboxAnchor>
            <ComboboxList class="w-[460px]">
              <ComboboxGroup class="max-h-60 overflow-y-auto w-full">
                <ComboboxItem
                  v-if="
                    newItemName &&
                    newItemName.trim() !== '' &&
                    !registeredItems.find(
                      (item) =>
                        item.name.toLowerCase().replace(/\s+/g, '') ===
                        newItemName.toLowerCase().replace(/\s+/g, ''),
                    )
                  "
                  :key="newItemName"
                  :value="{ id: -1, name: newItemName.trim() }"
                  @select="onItemSelected($event.detail.value)"
                >
                  Legg til "{{ newItemName.trim() }}"
                </ComboboxItem>
                <ComboboxItem
                  class="w-full"
                  v-for="item in registeredItems"
                  :key="item.id"
                  :value="item"
                  @select="onItemSelected(item)"
                >
                  {{ item.name }}
                  <ComboboxItemIndicator>
                    <Check class="ml-auto h-4 w-4" />
                  </ComboboxItemIndicator>
                </ComboboxItem>
              </ComboboxGroup>
            </ComboboxList>
          </Combobox>
        </div>
        <div class="flex gap-3">
          <div class="flex-1">
            <Label>Mengde</Label>
            <Input
              required
              v-model="itemAmount"
              placeholder="Mengde"
              type="number"
              :min="0"
              :disabled="!selectedItemName"
              @keydown="handleKeydown"
            />
          </div>
          <div class="flex-1">
            <Label>Enhet</Label>
            <Select required v-model="selectedItemUnit">
              <SelectTrigger :disabled="!selectedItemName || !isCustomItemSelected">
                <SelectValue placeholder="Velg enhet" />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  <SelectItem :value="1">stk</SelectItem>
                  <SelectItem :value="2">gram</SelectItem>
                  <SelectItem :value="3">liter</SelectItem>
                </SelectGroup>
              </SelectContent>
            </Select>
          </div>
        </div>
        <Label>Type</Label>
        <Select required v-model="selectedItemType">
          <SelectTrigger :disabled="!selectedItemName || !isCustomItemSelected">
            <SelectValue placeholder="Velg type" />
          </SelectTrigger>
          <SelectContent>
            <SelectGroup>
              <SelectItem :value="1">Mat og drikke</SelectItem>
              <SelectItem :value="2">Varme og lys</SelectItem>
              <SelectItem :value="3">Informasjon</SelectItem>
              <SelectItem :value="4">Legemidler og hygiene</SelectItem>
              <SelectItem :value="5">Annet</SelectItem>
            </SelectGroup>
          </SelectContent>
        </Select>
        <Label>Utløpsdato</Label>
        <Popover>
          <PopoverTrigger as-child :disabled="!selectedItemName">
            <Button
              type="button"
              variant="outline"
              :class="['justify-start font-normal', { 'text-muted-foreground': !expirationDate }]"
            >
              <CalendarIcon />
              {{
                expirationDate
                  ? dateFormat.format(expirationDate.toDate(getLocalTimeZone()))
                  : 'Velg dato'
              }}
            </Button>
          </PopoverTrigger>
          <PopoverContent>
            <Calendar v-model="expirationDate" initial-focus />
          </PopoverContent>
        </Popover>
        <DialogFooter>
          <Button type="submit">Legg til</Button>
        </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>

</template>

<style scoped></style>
