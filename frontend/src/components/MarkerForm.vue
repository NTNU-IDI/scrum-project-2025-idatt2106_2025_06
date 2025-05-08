<script setup>
import { ref, computed } from 'vue'
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

const selectedDays = ref([]);
const daysOfWeek = [
  { key: 'monday', label: 'Mandag' },
  { key: 'tuesday', label: 'Tirsdag' },
  { key: 'wednesday', label: 'Onsdag' },
  { key: 'thursday', label: 'Torsdag' },
  { key: 'friday', label: 'Fredag' },
  { key: 'saturday', label: 'Lørdag' },
  { key: 'sunday', label: 'Søndag' },
];

const title = ref('');
const latitude = ref('');
const longitude = ref('');
const type = ref('General');
const description = ref('');
const openingStart = ref('');
const openingEnd = ref('');
const contactInfo = ref({
  name: '',
  email: '',
  phone: ''
});


const combinedOpeningHours = computed(() => {
  if (openingStart.value && openingEnd.value) {
    return `${openingStart.value}-${openingEnd.value}`;
  }
  return '';
});

const openingHours = computed(() => {
  return daysOfWeek.reduce((acc, day) => {
    if (selectedDays.value.includes(day.key)) {
      acc[day.key] = combinedOpeningHours.value;
    } else {
      acc[day.key] = null;
    }
    return acc;
  }, {});
});

async function handleClear() {
  title.value = '';
  latitude.value = '';
  longitude.value = '';
  type.value = '';
  description.value = '';
  openingStart.value = '';
  openingEnd.value = '';
  selectedDays.value = [];
  contactInfo.value = {
    name: '',
    email: '',
    phone: ''
  };
}

const errors = ref({
  title: false,
  description: false,
  openingTime: false,
  days: false,
  latitude: false,
  longitude: false,
});


async function handleSubmit() {
  errors.value = {
    title: false,
    description: false,
    openingTime: false,
    days: false,
    latitude: false,
    longitude: false,
  };

  let hasError = false;

  if (!title.value.trim()) {
    errors.value.title = true;
    hasError = true;
  }

  if (!description.value.trim()) {
    errors.value.description = true;
    hasError = true;
  }

  if (!latitude.value.trim() || isNaN(parseFloat(latitude.value))) {
    errors.value.latitude = true;
    hasError = true;
  }

  if (!longitude.value.trim() || isNaN(parseFloat(longitude.value))) {
    errors.value.longitude = true;
    hasError = true;
  }

  const hasOpeningStart = !!openingStart.value;
  const hasOpeningEnd = !!openingEnd.value;

  if ((hasOpeningStart && !hasOpeningEnd) || (!hasOpeningStart && hasOpeningEnd)) {
    errors.value.openingTime = true;
    hasError = true;
  }

  if (hasOpeningStart && hasOpeningEnd && selectedDays.value.length === 0) {
    errors.value.days = true;
    hasError = true;
  }

  if (hasError) {
    return;
  }

  try {
    const requestBody = {
      name: title.value,
      description: description.value,
      type: type.value,
      location: {
        latitude: parseFloat(latitude.value),
        longitude: parseFloat(longitude.value)
      },
      openingHours: Object.keys(openingHours.value).reduce((acc, dayKey) => {
        if (openingHours.value[dayKey]) {
          acc[dayKey] = openingHours.value[dayKey];
        }
        return acc;
      }, {}),
      contactInfo: contactInfo.value,
    };

    console.log('Sending marker:', requestBody);
    await createMarker(requestBody);
    await handleClear();
  } catch (error) {
    console.error('Failed to create marker', error);
  }
}
</script>

<template>
  <Card class="flex flex-col gap-2 p-5">
    <div class="flex align-middle">
      <Label class="m-2" for="title">Tittel</Label>
      <Input
        v-model="title"
        :class="['border w-full', errors.title ? 'border-red-500' : '']"
        id="title"
        placeholder="Navn på markør"
      />

    </div>

    <div class="flex align-middle">
      <Label class="m-2" for="description">Beskrivelse</Label>
      <Input
        v-model="description"
        :class="['border w-full', errors.description ? 'border-red-500' : '']"
        id="description"
        placeholder="Kort innhold av markør"
      />

    </div>

    <div class="flex items-center">
      <Label class="m-2" for="latitude">Posisjon</Label>
      <div class="flex flex-col ">
        <Label class="m-2" for="latitude">Breddegrad</Label>
        <Input
          class="border w-full"
          :class="errors.latitude ? 'border-red-500' : ''"
          id="latitude"
          placeholder="eks: 64.232321"
          v-model="latitude"
        />
      </div>

      <div class="flex flex-col ">
        <Label class="m-2" for="longitude">Lengdegrad</Label>
        <Input
          class="border w-full"
          :class="errors.longitude ? 'border-red-500' : ''"
          id="longitude"
          placeholder="eks: 10.422132"
          v-model="longitude"
        />

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

    <div class="flex flex-col mt-4 gap-2">
      <div class="flex items-center gap-4">
      <Label class="m-2">Åpningstid</Label>
        <div class="flex flex-col">
          <Label class="m-2" for="opening-start">Åpner</Label>
          <input
            type="time"
            id="opening-start"
            v-model="openingStart"
            :class="['border rounded-md px-2 py-1', errors.openingTime ? 'border-red-500' : '']"
          />
        </div>
        <div class="flex flex-col">
          <Label class="m-2" for="opening-end">Stenger</Label>
          <input
            type="time"
            id="opening-end"
            v-model="openingEnd"
            :class="['border rounded-md px-2 py-1', errors.openingTime ? 'border-red-500' : '']"
          />
        </div>
      </div>
    </div>

    <div :class="['grid grid-cols-4 gap-2 p-2', errors.days ? 'border border-red-500 rounded-md' : '']">
      <div v-for="day in daysOfWeek" :key="day.key" class="flex text-gray-600 items-center gap-2">
        <input
          type="checkbox"
          :id="day.key"
          :value="day.key"
          v-model="selectedDays"
          class="accent-primary"
        />
        <Label :for="day.key">{{ day.label }}</Label>
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
    <Button variant="outline" class="flex-1" @click="handleClear">Avbryt</Button>
  </Card>
</template>
