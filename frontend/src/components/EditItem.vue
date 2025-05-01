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
const currentStorage = ref();
const props = defineProps({
  item: { type: Object, required: true },
  storageId: {type: Number, required: true}
});

onMounted(() => {
  currentItem.value = props.item;
  currentStorage.value = props.storageId;
  expirationDate.value = parseDate(currentItem.value.expirationDate);
  console.log(currentItem.value)
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
      <Input :model-value="currentItem.name" :disabled="true" placeholder="Navn" type="text" />
      <div class="flex gap-3">
        <div class="flex-1">
          <Label>Mengde</Label>
          <Input :model-value="currentItem.amount" placeholder="Mengde" type="text" />
        </div>
        <div class="flex-1">
          <Label>Enhet</Label>
          <Select :model-value="currentItem.unit">
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
      <Select :model-value="currentStorage">
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
