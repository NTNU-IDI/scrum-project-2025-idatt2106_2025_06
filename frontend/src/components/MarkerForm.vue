<script setup>
import { ref } from 'vue';
import { Button } from '@/components/ui/button/index.js';
import { Input } from '@/components/ui/input/index.js';
import { Card } from '@/components/ui/card/index.js';
import { Label } from '@/components/ui/label/index.js';
import {
  Select,
  SelectItem,
  SelectTrigger,
  SelectContent,
  SelectGroup,
  SelectValue
} from '@/components/ui/select/index.js'
import { createMarker } from '@/service/markerService.js'

const sharedOpeningHours = ref('')
const selectedDays = ref([])
const daysOfWeek = [
  { key: 'monday', label: 'Mandag' },
  { key: 'tuesday', label: 'Tirsdag' },
  { key: 'wednesday', label: 'Onsdag' },
  { key: 'thursday', label: 'Torsdag' },
  { key: 'friday', label: 'Fredag' },
  { key: 'saturday', label: 'Lørdag' },
  { key: 'sunday', label: 'Søndag' }
]

const title = ref('')
const latitude = ref('64.232321')
const longitude = ref('10.422132')
const type = ref('General')
const description = ref('')
const contactInfo = ref({
  name: '',
  email: '',
  phone: ''
})
const openingHours = daysOfWeek.reduce((acc, day) => {
  acc[day.key] = selectedDays.value.includes(day.key)
    ? sharedOpeningHours.value
    : null
  return acc
}, {})

async function handleSubmit() {
  try {
    const requestBody = {
      name: title.value,
      description: description.value,
      type: type.value,
      location: {
        latitude: parseFloat(latitude.value),
        longitude: parseFloat(longitude.value)
      },
      openingHours: openingHours.value
        ? { monday: openingHours.value }
        : null,
      contactInfo: contactInfo.value,
    }

    console.log('Sending marker:', requestBody)
    await createMarker(requestBody)
  } catch (error) {
    console.error('Failed to create marker', error)
  }
}

</script>

<template>
  <Card class="flex flex-col gap-2 p-5">
    <div class="flex align-middle">
      <Label class="m-2" for="title">Tittel</Label>
      <Input v-model="title" class="border w-full" id="title" placeholder="Navn på markør" />
    </div>

    <div class="flex align-middle">
      <Label class="m-2" for="description">Beskrivelse</Label>
      <Input v-model="description" class="border w-full" id="description" placeholder="Kort innhold av markør" />
    </div>

    <div class="flex items-center">
      <Label class="m-2" for="latitude">Posisjon</Label>
      <div class="flex flex-col ">
        <Label class="m-2" for="latitude">Breddegrad</Label>
        <Input class="border w-full" id="latitude" placeholder="eks: 64.232321" v-model="latitude" />
      </div>

      <div class="flex flex-col ">
        <Label class="m-2" for="longitude">Lengdegrad</Label>
        <Input class="border w-full" id="longitude" placeholder="eks: 10.422132" v-model="longitude" />
      </div>
    </div>

    <div class="flex align-middle items-center">
      <Label class="m-2" for="type">Stedstype</Label>
      <div class="flex gap-2 items-center">
        <Select v-model="type">
          <SelectTrigger class="w-[200px]">
            <SelectValue placeholder="Velg stedstype" />
          </SelectTrigger>
          <SelectContent>
            <SelectGroup>
              <SelectItem value="Shelter">Tilfluktsrom</SelectItem>
              <SelectItem value="Defibrillator">Hjertestarter</SelectItem>
              <SelectItem value="EmergencyClinic">Legevakt</SelectItem>
              <SelectItem value="DistributionPoint">Utdelingssted</SelectItem>
              <SelectItem value="PoliceStation">Politistasjon</SelectItem>
              <SelectItem value="Pharmacy">Apotek</SelectItem>
              <SelectItem value="General">Annet</SelectItem>
            </SelectGroup>
          </SelectContent>
        </Select>
      </div>
    </div>

    <div class="flex align-middle mt-4">
      <Label class="m-2" for="shared-opening-hours">Åpningstid</Label>
      <Input
        v-model="sharedOpeningHours"
        class="border w-full"
        id="shared-opening-hours"
        placeholder="Eks 08:00-16:00"
      />
    </div>

    <div class="flex flex-col gap-2">
      <Label class="m-2">Velg dager åpningstiden gjelder for</Label>
      <div class="grid grid-cols-4 gap-2">
        <div v-for="day in daysOfWeek" :key="day.key" class="flex text-gray-600 items-center gap-2">
          <input
            type="checkbox"
            :id="day.key"
            :value="day.key"
            v-model="selectedDays"
            class="accent-primary "
          />
          <Label :for="day.key">{{ day.label }}</Label>
        </div>
      </div>
    </div>

    <Label class="m-2" for="contact-info">Kontaktinfo</Label>
    <div class="flex items-center">
      <Label class="m-2 w-21" for="contact-name">Navn</Label>
      <Input v-model="contactInfo.name" class="border flex-1" id="contact-name" placeholder="Navn på kontaktperson" />
    </div>

    <div class="flex items-center">
      <Label class="m-2 w-21" for="contact-email">E-post</Label>
      <Input v-model="contactInfo.email" class="border flex-1" id="contact-email" placeholder="E-postadresse" />
    </div>

    <div class="flex items-center">
      <Label class="m-2 21" for="contact-phone">Telefon</Label>
      <Input v-model="contactInfo.phone" class="border flex-1" id="contact-phone" placeholder="Telefonnummer" />
    </div>

    <Button class="flex-1" @click="handleSubmit">Plasser markør</Button>
    <Button variant="destructive" class="flex-1">Slett</Button>
  </Card>
</template>
