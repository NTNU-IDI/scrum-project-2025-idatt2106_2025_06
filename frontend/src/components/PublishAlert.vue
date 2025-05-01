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

import { Button } from "@/components/ui/button/index.js";
import { Input } from "@/components/ui/input/index.js";
import { Label } from "@/components/ui/label/index.js";
import { DialogClose } from "@/components/ui/dialog/index.js";
import { ref, onMounted } from 'vue'

const title = ref('')
const description = ref('')
const alertType = ref('info')
const alertDate = ref('')
const alertTime = ref('')

const emit = defineEmits(['update'])

function publishAlert() {
  // Når "Publiser" trykkes, send nytt varsel til backend
  console.log('Publisert varsel:', {
    title: title.value,
    description: description.value,
    type: alertType.value,
    date: alertDate.value,
    time: alertTime.value,
  })
  emit('update', {
    title: title.value,
    description: description.value,
    type: alertType.value,
    date: alertDate.value,
    time: alertTime.value,
  })
}

// Sett dato og tid til sanntid når komponenten lastes
onMounted(() => {
  const currentDate = new Date();
  const formattedDate = currentDate.toISOString().split('T')[0]; // Formater datoen som YYYY-MM-DD
  const formattedTime = currentDate.toTimeString().split(' ')[0].slice(0, 5); // Formater tiden som HH:MM

  alertDate.value = formattedDate;
  alertTime.value = formattedTime;
});
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <Button class="p-2">
        Nytt varsel
      </Button>
    </DialogTrigger>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Publiser nytt varsel</DialogTitle>

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
        <Button class="flex-1" variant="default" @click="publishAlert">
          Publiser
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
