<script setup>

import { DateFormatter, getLocalTimeZone } from '@internationalized/date'
import {
  Dialog,
  DialogClose,
  DialogContent, DialogDescription, DialogFooter,
  DialogHeader, DialogTitle,
  DialogTrigger
} from '@/components/ui/dialog/index.js'
import { Calendar } from '@/components/ui/calendar/index.js'
import { CalendarIcon } from 'lucide-vue-next'
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
import { ref, watch } from 'vue'

const props = defineProps({
  storageId: { type: Number, required: true }
});

const dateFormat = new DateFormatter('nb-NO', {
  dateStyle: 'long',
})

const expirationDate = ref();
const storage = ref();

watch(() => props.storageId, () => {
  storage.value = props.storageId;
})
</script>

<template>
  <Dialog>
    <DialogTrigger>
      <button>
        <Button>Legg til</Button>
      </button>
    </DialogTrigger>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Legg til ny vare</DialogTitle>
        <DialogDescription></DialogDescription>
      </DialogHeader>
      <Label>Navn på vare</Label>
      <Input placeholder="Navn" type="text" />
      <div class="flex gap-3">
        <div class="flex-1">
          <Label>Mengde</Label>
          <Input placeholder="Mengde" type="text" />
        </div>
        <div class="flex-1">
          <Label>Enhet</Label>
          <Select>
            <SelectTrigger>
              <SelectValue placeholder="Velg enhet" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="1">stk</SelectItem>
                <SelectItem value="2">gram</SelectItem>
                <SelectItem value="3">liter</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
      </div>
      <Label>Type</Label>
      <Select v-model="storage">
        <SelectTrigger>
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
          <Button type="submit">Legg til</Button>
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<style scoped>

</style>
