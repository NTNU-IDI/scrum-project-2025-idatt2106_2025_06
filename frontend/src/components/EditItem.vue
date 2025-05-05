<script setup>

import {
  Dialog,
  DialogClose,
  DialogContent, DialogDescription, DialogFooter,
  DialogHeader, DialogTitle,
  DialogTrigger
} from '@/components/ui/dialog/index.js'
import { Pencil, CalendarIcon } from 'lucide-vue-next'
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
import { onMounted, ref } from 'vue'
import {
  DateFormatter,
  getLocalTimeZone, parseDate
} from '@internationalized/date'

const dateFormat = new DateFormatter('nb-NO', {
  dateStyle: 'long',
})

const expirationDate = ref();

const currentItem = ref();
const itemName = ref();
const itemAmount = ref();
const itemType = ref();
const itemUnit = ref();
const props = defineProps({
  item: { type: Object, required: true },
  typeId: {type: Number, required: true}
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

onMounted(() => {
  currentItem.value = props.item;
  itemType.value = props.typeId;
  itemName.value = currentItem.value.name;
  itemAmount.value = String(currentItem.value.amount);
  itemUnit.value = currentItem.value.unit;
  expirationDate.value = parseDate(currentItem.value.expirationDate);
})
</script>

<template>
  <Dialog>
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
      <Label>Navn på vare</Label>
      <Input v-model="itemName" :disabled="true" placeholder="Navn" type="text" />
      <div class="flex gap-3">
        <div class="flex-1">
          <Label>Mengde</Label>
          <Input v-model="itemAmount"
                 placeholder="Mengde"
                 type="text"
                 inputmode="decimal"
                 @input="handleAmountInput"
          />
        </div>
        <div class="flex-1">
          <Label>Enhet</Label>
          <Select v-model="itemUnit">
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
      <Select v-model="itemType">
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
      <Popover>
        <PopoverTrigger as-child>
          <Button
            variant="outline"
            :class="['justify-start font-normal',
            { 'text-muted-foreground' : !expirationDate},
            ]">
            <CalendarIcon />
            {{ expirationDate ? dateFormat.format(expirationDate.toDate(getLocalTimeZone())) : 'Velg dato' }}
          </Button>
        </PopoverTrigger>
        <PopoverContent>
          <Calendar v-model="expirationDate" initial-focus/>
        </PopoverContent>
      </Popover>
      <DialogFooter>
        <DialogClose>
          <Button type="submit">Endre</Button>
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<style scoped>

</style>
