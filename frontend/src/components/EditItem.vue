<script setup>

import {
  Dialog,
  DialogContent, DialogDescription, DialogFooter,
  DialogHeader, DialogTitle,
  DialogTrigger
} from '@/components/ui/dialog/index.js'
import { Pencil, CalendarIcon, X } from 'lucide-vue-next'
import {
  Select,
  SelectContent,
  SelectGroup, SelectItem,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select/index.js'
import { Label } from '@/components/ui/label/index.js'
import { Input } from '@/components/ui/input/index.js'
import { Button } from '@/components/ui/button/index.js'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover/index.js'
import { Calendar } from '@/components/ui/calendar/index.js'
import { onMounted, ref, watch } from 'vue'
import {
  DateFormatter,
  getLocalTimeZone, parseDate
} from '@internationalized/date'
import { useInventoryStore } from '@/stores/inventory.js'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip/index.js'

const dateFormat = new DateFormatter('nb-NO', {
  dateStyle: 'long',
})

const inventoryStore = useInventoryStore();

const isNewItemDialogOpen = ref();

const expirationDate = ref();

const currentItem = ref();
const itemId = ref();
const itemName = ref();
const itemAmount = ref();
const itemType = ref();
const itemUnit = ref();
const props = defineProps({
  item: { type: Object, required: true },
  typeId: {type: Number, required: true},
  storageId: { type: String, required: true },
});

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

const handleEdit = () => {
  let formattedExpDate;
  if (expirationDate.value) {
    formattedExpDate = new Date(expirationDate.value).toISOString().split('T')[0]
  }
  inventoryStore.editItemInstance(itemId.value, itemAmount.value, formattedExpDate,
    props.storageId, itemType.value);

  isNewItemDialogOpen.value = false;
}

watch(
  () => props.item,
  () => {
    currentItem.value = props.item
    currentItem.value = props.item;
    itemType.value = props.typeId;
    itemId.value = currentItem.value.id;
    itemName.value = currentItem.value.name;
    itemAmount.value = String(currentItem.value.amount);
    itemUnit.value = currentItem.value.unitId;
    expirationDate.value = currentItem.value.expiryDate ?
      parseDate(currentItem.value.expiryDate) : '';
  },
)

watch(isNewItemDialogOpen, (value) => {
  if (!value) {
      itemAmount.value = String(currentItem.value.amount);
      expirationDate.value = currentItem.value.expiryDate ?
        parseDate(currentItem.value.expiryDate) : '';
    }
  },
)
onMounted(() => {
  currentItem.value = props.item;
  itemType.value = props.typeId;
  itemId.value = currentItem.value.id;
  itemName.value = currentItem.value.name;
  itemAmount.value = String(currentItem.value.amount);
  itemUnit.value = currentItem.value.unitId;
  expirationDate.value = currentItem.value.expiryDate ?
    parseDate(currentItem.value.expiryDate) : '';
})
</script>

<template>
  <Dialog v-model:open="isNewItemDialogOpen">
    <DialogTrigger>
      <button>
        <Pencil/>
      </button>
    </DialogTrigger>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Endre på vare</DialogTitle>
        <DialogDescription/>
      </DialogHeader>
      <form @submit.prevent="handleEdit" class="contents">
      <Label>Navn på vare</Label>
      <Input v-model="itemName" required :disabled="true" placeholder="Navn" type="text" />
      <div class="flex gap-3">
        <div class="flex-1">
          <Label>Mengde</Label>
          <Input v-model="itemAmount"
                 required
                 placeholder="Mengde"
                 type="text"
                 inputmode="decimal"
                 @input="handleAmountInput"
          />
        </div>
        <div class="flex-1">
          <Label>Enhet</Label>
          <Select required v-model="itemUnit">
            <SelectTrigger :disabled="true">
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
      <Select required v-model="itemType">
        <SelectTrigger :disabled="true">
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
            <PopoverTrigger as-child>
              <Button
                variant="outline"
                class="w-full justify-start font-normal"
                :class="{ 'text-muted-foreground': !expirationDate }">
                <CalendarIcon />
                {{ expirationDate ? dateFormat.format(expirationDate.toDate(getLocalTimeZone())) : 'Velg dato' }}
              </Button>
            </PopoverTrigger>
            <PopoverContent>
              <Calendar v-model="expirationDate" initial-focus/>
            </PopoverContent>
          </Popover>
        </div>
        <div>
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger>
                <Button type="button" @click.prevent="expirationDate = ''" variant="outline">
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
        <Button type="submit" :disabled="!itemAmount">Endre</Button>
      </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>

<style scoped>

</style>
