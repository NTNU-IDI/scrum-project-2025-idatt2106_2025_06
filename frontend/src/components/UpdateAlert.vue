<script setup>
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import {
  Select,
  SelectItem,
  SelectTrigger,
  SelectContent,
  SelectValue
} from '@/components/ui/select/index.js'

import {Button} from "@/components/ui/button/index.js";
import {Input} from "@/components/ui/input/index.js";
import {Label} from "@/components/ui/label/index.js";
import {DialogClose} from "@/components/ui/dialog/index.js";
import { Pencil } from 'lucide-vue-next'
import { ref, watchEffect } from 'vue'

const props = defineProps({
  title: String,
  description: String,
  date: String,
  time: String,
  type: String,
  variant: String
})

const title = ref('')
const description = ref('')
const alertType = ref('')
const alertDate = ref('')
const alertTime = ref('')

watchEffect(() => {
  title.value = props.title || ''
  description.value = props.description || ''
  alertType.value = props.type || ''
  alertDate.value = props.date || ''
  alertTime.value = props.time || ''
  }
)

const emit = defineEmits(['update'])

function updateAlert() {
  console.log('Updated alert:', {
    title: title.value,
    description: description.value,
    type: alertType.value,
    date: alertDate.value,
    time: alertTime.value,
  })
  emit('update', updated)
  //TODO: send til backend
}
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <Button class="p-2" variant="outline">
        <Pencil />
      </Button>
    </DialogTrigger>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Oppdater varsling</DialogTitle>

        <div class="flex items-center">
          <Label class="m-2 w-24" for="title">Navn:</Label>
          <Input id="title" v-model="title" placeholder="Navn på varsel" />
        </div>

        <div class="flex items-center">
          <Label class="m-2 w-24" for="text">Varsel:</Label>
          <Input id="text" v-model="description" placeholder="Innhold for varsel" />
        </div>

        <div class="flex items-center">
          <Label class="m-2" for="type">Beredskapsnivå</Label>
          <!-- Dropdown for valg av type -->
          <div class="flex gap-2 items-center">
            <Select id="type" v-model="alertType">
              <SelectTrigger class="w-[180px]">
                <SelectValue placeholder="Velg beredskapsnivå" />
              </SelectTrigger>
              <SelectContent>
                  <SelectItem value="info" severity="info">Info</SelectItem>
                  <SelectItem value="green" severity="green">Lav</SelectItem>
                  <SelectItem value="yellow" severity="yellow">Middels</SelectItem>
                  <SelectItem value="red" severity="red">Høy</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </div>

        <div class="flex gap-2 items-center">
          <Label class="m-2 w-24" for="time">Tidspunkt:</Label>
          <!-- Kalender for dato -->
          <input
            type="date"
            id="start-date"
            v-model="alertDate"
            class="p-2 border rounded-md focus:ring-2 focus:ring-blue-500"
          />
          <!-- Klokke for tid -->
          <input
            type="time"
            id="start-time"
            v-model="alertTime"
            class="p-2 border rounded-md focus:ring-2 focus:ring-blue-500"
          />
        </div>
      </DialogHeader>


      <DialogFooter class="flex gap-2">
        <DialogClose>
          <Button variant="outline" class="flex-1">
            Avbryt
          </Button>
        </DialogClose>
        <Button class="flex-1" variant="default" @click="updateAlert">
          Oppdater
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
