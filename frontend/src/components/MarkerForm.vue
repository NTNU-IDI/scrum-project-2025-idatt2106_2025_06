<template>
  <Card class="flex flex-col p-5">
    <div class="flex flex-col gap-2">
      <div class="flex items-center">
        <Label class="m-2" for="title">Tittel</Label>
        <Input
          id="title"
          v-model="title"
          :class="['border w-full', errors.title ? 'border-red-500' : '']"
          placeholder="Navn på markør"
        />
      </div>
      <div class="flex items-center">
        <Label class="m-2" for="description">Beskrivelse</Label>
        <Input
          id="description"
          v-model="description"
          :class="['border w-full', errors.description ? 'border-red-500' : '']"
          placeholder="Kort innhold av markør"
        />
      </div>
    </div>

    <div class="flex flex-col gap-2">
      <div class="flex items-center">
        <Label class="m-2" for="latitude">Posisjon</Label>
        <div class="flex flex-col flex-1 mr-2">
          <Label class="m-2" for="latitude">Breddegrad</Label>
          <Input
            id="latitude"
            v-model="latitude"
            :class="['border w-full', errors.latitude ? 'border-red-500' : '']"
            placeholder="eks: 64.232321"
          />
        </div>
        <div class="flex flex-col flex-1">
          <Label class="m-2" for="longitude">Lengdegrad</Label>
          <Input
            id="longitude"
            v-model="longitude"
            :class="['border w-full', errors.longitude ? 'border-red-500' : '']"
            placeholder="eks: 10.422132"
          />
        </div>
      </div>

      <div class="flex items-center">
        <Label class="m-2" for="type">Stedstype</Label>
        <Select v-model="type" class="w-[200px]">
          <SelectTrigger>
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

    <div class="flex items-center gap-4 mt-4">
      <Label class="m-2">Åpningstid</Label>
      <div class="flex flex-col">
        <Label class="m-2" for="opening-start">Åpner</Label>
        <input
          id="opening-start"
          v-model="openingStart"
          :class="['border rounded-md px-2 py-1', errors.openingTime ? 'border-red-500' : '']"
          type="time"
        />
      </div>
      <div class="flex flex-col">
        <Label class="m-2" for="opening-end">Stenger</Label>
        <input
          id="opening-end"
          v-model="openingEnd"
          :class="['border rounded-md px-2 py-1', errors.openingTime ? 'border-red-500' : '']"
          type="time"
        />
      </div>
    </div>

    <div
      :class="['grid grid-cols-4 gap-2 p-2', errors.days ? 'border border-red-500 rounded-md' : '']"
    >
      <div v-for="day in daysOfWeek" :key="day.key" class="flex items-center gap-2">
        <input
          :id="day.key"
          v-model="selectedDays"
          :value="day.key"
          class="accent-primary"
          type="checkbox"
        />
        <Label :for="day.key">{{ day.label }}</Label>
      </div>
    </div>

    <div class="flex flex-col gap-2">
      <Label class="m-2" for="contact-info">Kontaktinfo</Label>
      <div class="flex items-center gap-2">
        <Label class="m-2 w-24" for="contact-name">Navn</Label>
        <Input
          id="contact-name"
          v-model="contactInfo.name"
          class="border flex-1"
          placeholder="Navn på kontaktperson"
        />
      </div>
      <div class="flex items-center gap-2">
        <Label class="m-2 w-24" for="contact-email">E-post</Label>
        <Input
          id="contact-email"
          v-model="contactInfo.email"
          class="border flex-1"
          placeholder="E-postadresse"
        />
      </div>
      <div class="flex items-center gap-2">
        <Label class="m-2 w-24" for="contact-phone">Telefon</Label>
        <Input
          id="contact-phone"
          v-model="contactInfo.phone"
          class="border flex-1"
          placeholder="Telefonnummer"
        />
      </div>
    </div>

    <div class="flex gap-2 mt-4">
      <Button class="flex-1" @click="handleSubmit">
        {{ props.mode === 'edit-marker' ? 'Oppdater markør' : 'Plasser markør' }}
      </Button>
      <Button
        v-if="props.mode === 'edit-marker'"
        class="flex-1 bg-red-600 text-white hover:bg-red-700 hover:text-white"
        variant="outline"
        @click="handleDelete"
        >Slett markør
      </Button>
      <Button
        id="clearButton"
        v-if="props.mode === 'edit-marker'"
        class="flex-1"
        variant="outline"
        @click="handleClear"
        >Avbryt
      </Button>
    </div>
  </Card>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { Button } from '@/components/ui/button/index.js'
import { Input } from '@/components/ui/input/index.js'
import { Card } from '@/components/ui/card/index.js'
import { Label } from '@/components/ui/label/index.js'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select/index.js'
import { createMarker, deleteMarker, updateMarker } from '@/service/markerService.js'

const props = defineProps({
  mode: { type: String, default: 'new' }, // 'new' or 'edit-marker'
  markerData: { type: Object, default: null },
})

const title = ref('')
const description = ref('')
const latitude = ref('')
const longitude = ref('')
const type = ref('General')
const openingStart = ref('')
const openingEnd = ref('')
const selectedDays = ref([])
const contactInfo = ref({ name: '', email: '', phone: '' })
const mode = ref(props.mode)

const daysOfWeek = [
  { key: 'monday', label: 'Mandag' },
  { key: 'tuesday', label: 'Tirsdag' },
  { key: 'wednesday', label: 'Onsdag' },
  { key: 'thursday', label: 'Torsdag' },
  { key: 'friday', label: 'Fredag' },
  { key: 'saturday', label: 'Lørdag' },
  { key: 'sunday', label: 'Søndag' },
]

const combinedOpeningHours = computed(() => {
  return openingStart.value && openingEnd.value ? `${openingStart.value}-${openingEnd.value}` : ''
})

const errors = ref({
  title: false,
  description: false,
  latitude: false,
  longitude: false,
  openingTime: false,
  days: false,
})

watch(
  () => props.markerData,
  (m) => {
    if (props.mode === 'edit-marker' && m) {
      title.value = m.name
      description.value = m.description
      type.value = m.type
      latitude.value = String(m.location.latitude)
      longitude.value = String(m.location.longitude)
      openingStart.value = m.openingHours[Object.keys(m.openingHours)[0]].split('-')[0]
      openingEnd.value = m.openingHours[Object.keys(m.openingHours)[0]].split('-')[1]
      selectedDays.value = Object.keys(m.openingHours)
        .filter((day) => m.openingHours[day] === combinedOpeningHours.value)
        .map((day) => day.toLowerCase())
      contactInfo.value = m.contactInfo || { name: '', email: '', phone: '' }
    }
  },
  { immediate: true },
)

function handleClear() {
  title.value = ''
  description.value = ''
  latitude.value = ''
  longitude.value = ''
  type.value = 'General'
  openingStart.value = ''
  openingEnd.value = ''
  selectedDays.value = []
  contactInfo.value = { name: '', email: '', phone: '' }
  mode.value = 'new'
  emit('cancel')
}

async function handleDelete() {
  if (props.markerData) {
    await deleteMarker(props.markerData.id)
    emit('saved')
    handleClear()
  }
}

const emit = defineEmits(['saved', 'cancel'])

async function handleSubmit() {
  errors.value = {
    title: false,
    description: false,
    latitude: false,
    longitude: false,
    openingTime: false,
    days: false,
  }
  let hasError = false

  if (!title.value.trim()) {
    errors.value.title = true
    hasError = true
  }
  if (!description.value.trim()) {
    errors.value.description = true
    hasError = true
  }
  if (isNaN(parseFloat(latitude.value))) {
    errors.value.latitude = true
    hasError = true
  }
  if (isNaN(parseFloat(longitude.value))) {
    errors.value.longitude = true
    hasError = true
  }

  const hasStart = !!openingStart.value
  console.log(openingStart.value)
  const hasEnd = !!openingEnd.value
  console.log(openingEnd.value)
  console.log(hasStart, hasEnd)
  if (hasStart !== hasEnd) {
    errors.value.openingTime = true
    hasError = true
  }
  if (hasStart && hasEnd && selectedDays.value.length === 0) {
    errors.value.days = true
    hasError = true
  }

  if (hasError) return

  const body = {
    name: title.value,
    description: description.value,
    type: type.value,
    location: {
      latitude: parseFloat(latitude.value),
      longitude: parseFloat(longitude.value),
    },
    openingHours: daysOfWeek.reduce((acc, day) => {
      if (selectedDays.value.includes(day.key)) {
        acc[day.key] = combinedOpeningHours.value
      }
      return acc
    }, {}),
    contactInfo: contactInfo.value,
  }

  try {
    if (mode.value === 'edit-marker') {
      body.id = props.markerData.id
      await updateMarker(body)
    } else {
      await createMarker(body)
    }
    handleClear()
    emit('saved')
  } catch (e) {
    console.error('Kunne ikke lagre markør:', e)
  }
}
</script>
