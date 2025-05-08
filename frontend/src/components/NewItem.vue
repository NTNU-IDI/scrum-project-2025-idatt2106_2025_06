<script setup>
import { DateFormatter, getLocalTimeZone } from '@internationalized/date'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog/index.js'
import { Calendar } from '@/components/ui/calendar/index.js'
import { CalendarIcon } from 'lucide-vue-next'
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
import { computed, onMounted, ref, watch } from 'vue'

import {
  Combobox,
  ComboboxAnchor,
  ComboboxGroup,
  ComboboxInput,
  ComboboxItem,
  ComboboxItemIndicator,
  ComboboxList,
} from '@/components/ui/combobox'
import { Check, Search, X } from 'lucide-vue-next'
import { useInventoryStore } from '@/stores/inventory.js'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip/index.js'

const props = defineProps({
  typeId: { type: Number, required: true },
  storageId: { type: String, required: true },
})

const dateFormat = new DateFormatter('nb-NO', {
  dateStyle: 'long',
})

const expirationDate = ref()

const inventoryStore = useInventoryStore();
const registeredItems = computed(() => inventoryStore.registeredItems);

const isNewItemDialogOpen = ref();

const newItemName = ref()
const itemAmount = ref()
const selectedItemId = ref()
const selectedItemName = ref()
const selectedItemType = ref()
const selectedItemUnit = ref()

const isCustomItemSelected = ref(false)
const onItemSelected = (item) => {
  if (item.id === -1) {
    isCustomItemSelected.value = true
    selectedItemUnit.value = ''
    selectedItemType.value = props.typeId
  } else {
    isCustomItemSelected.value = false
    selectedItemUnit.value = item.unitId
    selectedItemType.value = item.typeId
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
  let formattedExpDate;
  if (expirationDate.value) {
    formattedExpDate = new Date(expirationDate.value).toISOString().split('T')[0]
  }

  inventoryStore.addNew(selectedItemName.value, selectedItemType.value, selectedItemUnit.value,
    props.storageId, itemAmount.value, formattedExpDate, props.typeId);
  refreshFields();
  isNewItemDialogOpen.value = false;
}

function handleAmountInput(e) {
  let value = e.target.value;
  value = value.replace(/[^\d.,]/g, '');
  value = value.replace(',', '.');
  const parts = value.split('.');
  if (parts.length > 2) {
    value = parts[0] + '.' + parts.slice(1).join('');
  }
  value = value.replace(/^0+([1-9])/, '$1');
  itemAmount.value = value;
}

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
  inventoryStore.getAllRegisteredItems();
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
          <DialogDescription></DialogDescription>
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
              type="text"
              inputmode="decimal"
              :disabled="!selectedItemName"
              @input="handleAmountInput"
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
        <div class="flex gap-3 content justify-between">
          <div class="w-full">
            <Popover>
              <PopoverTrigger as-child :disabled="!selectedItemName">
                <Button
                  type="button"
                  variant="outline"
                  class="w-full justify-start font-normal"
                  :class="{ 'text-muted-foreground': !expirationDate }"
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
          </div>
          <div>
            <TooltipProvider :disabled="!selectedItemName">
              <Tooltip>
                <TooltipTrigger :disabled="!selectedItemName">
                  <Button type="button" @click.prevent="expirationDate = ''" variant="outline" :disabled="!selectedItemName">
                    <X />
                  </Button>
                </TooltipTrigger>
                <TooltipContent>
                  <p>Fjern dato</p>
                </TooltipContent>
              </Tooltip>
            </TooltipProvider>
          </div>
        </div>
        <DialogFooter>
          <Button type="submit" :disabled="!newItemName || !itemAmount || !selectedItemUnit || !selectedItemType">
            Legg til
          </Button>
        </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>

</template>

<style scoped></style>
