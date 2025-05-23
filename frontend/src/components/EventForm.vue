<script setup>
import { defineProps, ref, watch } from 'vue'
import { useEventStore } from '@/stores/event.js'
import { Card } from '@/components/ui/card/index.js'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select/index.js'

const eventStore = useEventStore()

const props = defineProps({
  token: { type: String, required: true },
  mode: { type: String, default: 'new' }, // 'new' or 'edit'
  eventData: Object,
})

const emit = defineEmits(['saved', 'cancel'])

// local state & refs
const mode = ref(props.mode)
const id = ref(null)
const title = ref('')
const description = ref('')
const content = ref('')
const latitude = ref('')
const longitude = ref('')
const impactAreaRadius = ref(0)
const recommendedEvacuationRadius = ref(0)
const mandatoryEvacuationRadius = ref(0)
const eventType = ref('other')
const eventStatus = ref('ongoing')
const selectedSeverity = ref('low')

// clear / cancel
function eventFormRefresh() {
  id.value = null
  title.value = ''
  description.value = ''
  content.value = ''
  latitude.value = ''
  longitude.value = ''
  impactAreaRadius.value = 0
  recommendedEvacuationRadius.value = 0
  mandatoryEvacuationRadius.value = 0
  eventType.value = 'other'
  eventStatus.value = 'ongoing'
  selectedSeverity.value = 'low'
  mode.value = 'new'
  emit('cancel')
}

// validation
const errors = ref({
  title: false,
  description: false,
  latitude: false,
  longitude: false,
  severity: false,
  eventType: false,
})

function validateForm() {
  errors.value = {
    title: false,
    description: false,
    latitude: false,
    longitude: false,
    severity: false,
    eventType: false,
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
  const latOk = latitude.value && !isNaN(parseFloat(latitude.value))
  const lngOk = longitude.value && !isNaN(parseFloat(longitude.value))
  if ((latitude.value || longitude.value) && !(latOk && lngOk)) {
    errors.value.latitude = true
    errors.value.longitude = true
    hasError = true
  }
  if (!selectedSeverity.value) {
    errors.value.severity = true
    hasError = true
  }
  if (!eventType.value) {
    errors.value.eventType = true
    hasError = true
  }
  return hasError
}

async function handleSubmit() {
  if (validateForm()) return
  const now = new Date().toISOString()
  const payload = {
    name: title.value,
    description: description.value,
    content: content.value,
    status: eventStatus.value,
    location: {
      latitude: parseFloat(latitude.value),
      longitude: parseFloat(longitude.value),
    },
    impact_area_radius_km: parseFloat(impactAreaRadius.value),
    recommended_evacuation_area_radius_km: parseFloat(recommendedEvacuationRadius.value),
    mandatory_evacuation_area_radius_km: parseFloat(mandatoryEvacuationRadius.value),
    type: eventType.value,
    severity: selectedSeverity.value,
    startTime: now,
    endTime: now,
  }

  try {
    if (mode.value === 'new') {
      await eventStore.createNewEvent(payload, props.token)
    } else {
      await eventStore.updateExistingEvent(id.value, payload, props.token)
    }
    emit('saved')
    eventFormRefresh()
  } catch (err) {
    console.error('Error submitting event:', err)
  }
}

async function handleDelete() {
  if (mode.value === 'edit' && id.value != null) {
    try {
      await eventStore.deleteEventById(id.value, props.token)
      emit('saved')
      eventFormRefresh()
    } catch (err) {
      console.error('Error deleting event:', err)
    }
  }
}

// seed form on edit
watch(
  () => props.eventData,
  (ev) => {
    if (ev) {
      mode.value = 'edit'
      id.value = ev.eventId
      title.value = ev.name
      description.value = ev.description
      content.value = ev.content
      latitude.value = ev.location?.latitude?.toString() || ''
      longitude.value = ev.location?.longitude?.toString() || ''
      impactAreaRadius.value = ev.impact_area_radius_km || 0
      recommendedEvacuationRadius.value = ev.recommended_evacuation_area_radius_km || 0
      mandatoryEvacuationRadius.value = ev.mandatory_evacuation_area_radius_km || 0
      eventType.value = ev.type || 'other'
      eventStatus.value = ev.status || 'ongoing'
      selectedSeverity.value = ev.severity || 'low'
    }
  },
  { immediate: true },
)
</script>

<template>
  <Card class="flex flex-col p-5">
    <div class="flex flex-col gap-2">
      <div class="flex items-center">
        <Label class="m-2" for="title">Tittel</Label>
        <Input
          id="title"
          v-model="title"
          :class="['border w-full', errors.title ? 'border-red-500' : '']"
          placeholder="Navn på hendelse"
        />
      </div>
      <div class="flex items-center">
        <Label class="m-2" for="description">Beskrivelse</Label>
        <Input
          id="description"
          v-model="description"
          :class="['border w-full', errors.description ? 'border-red-500' : '']"
          placeholder="Kort beskrivelse"
        />
      </div>
    </div>

    <div class="flex items-center">
      <Label class="m-2">Posisjon</Label>
      <div class="flex flex-col mr-2">
        <Label class="m-2" for="latitude">Breddegrad</Label>
        <Input
          id="latitude"
          v-model="latitude"
          :class="['border', errors.latitude ? 'border-red-500' : '']"
          placeholder="eks: 64.232321"
        />
      </div>
      <div class="flex flex-col">
        <Label class="m-2" for="longitude">Lengdegrad</Label>
        <Input
          id="longitude"
          v-model="longitude"
          :class="['border', errors.longitude ? 'border-red-500' : '']"
          placeholder="eks: 10.422132"
        />
      </div>
    </div>
    <div class="flex flex-col mt-2 gap-2">
      <div class="flex items-center">
        <Label class="m-2" for="severity">Farenivå</Label>
        <Select v-model="selectedSeverity" :class="errors.severity ? 'border-red-500' : ''">
          <SelectTrigger class="w-[180px]">
            <SelectValue placeholder="Velg nivå" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="low">Lav</SelectItem>
            <SelectItem value="medium">Middels</SelectItem>
            <SelectItem value="high">Høy</SelectItem>
          </SelectContent>
        </Select>
      </div>
      <div class="flex items-center">
        <Label class="m-2" for="eventStatus">Status</Label>
        <Select v-model="eventStatus">
          <SelectTrigger class="w-[180px]">
            <SelectValue placeholder="Velg status" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="planned">Planlagt</SelectItem>
            <SelectItem value="ongoing">Pågår</SelectItem>
            <SelectItem value="finished">Ferdig</SelectItem>
            <SelectItem value="cancelled">Avbrutt</SelectItem>
          </SelectContent>
        </Select>
      </div>
      <div class="flex items-center">
        <Label class="m-2" for="eventType">Hendelse</Label>
        <Select v-model="eventType" :class="errors.eventType ? 'border-red-500' : ''">
          <SelectTrigger class="w-[180px]">
            <SelectValue placeholder="Velg type" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="natural_disaster">Naturkatastrofe</SelectItem>
            <SelectItem value="nuclear_attack">Atomangrep</SelectItem>
            <SelectItem value="terror_attack">Terrorangrep</SelectItem>
            <SelectItem value="pandemic">Pandemi</SelectItem>
            <SelectItem value="other">Annet</SelectItem>
          </SelectContent>
        </Select>
      </div>
    </div>
    <div class="flex flex-col gap-2 mt-2">
      <div class="flex items-center gap-2">
        <Label class="m-2" for="impactAreaRadius">Impact radius (km)</Label>
        <Input
          id="impactAreaRadius"
          v-model.number="impactAreaRadius"
          class="border rounded px-2 py-1 w-24"
          type="number"
        />
      </div>
      <div class="flex items-center gap-2">
        <Label class="m-2" for="recommendedEvacuationRadius">Anbefalt evak.r. (km)</Label>
        <Input
          id="recommendedEvacuationRadius"
          v-model.number="recommendedEvacuationRadius"
          class="border rounded px-2 py-1 w-24"
          type="number"
        />
      </div>
      <div class="flex items-center gap-2">
        <Label class="m-2" for="mandatoryEvacuationRadius">Oblig.ev.r. (km)</Label>
        <Input
          id="mandatoryEvacuationRadius"
          v-model.number="mandatoryEvacuationRadius"
          class="border rounded px-2 py-1 w-24"
          type="number"
        />
      </div>
    </div>

    <div class="flex mt-4">
      <Label class="m-2" for="content">Innhold</Label>
      <Textarea
        id="content"
        v-model="content"
        class="border w-full"
        placeholder="Ytterligere detaljer"
      />
    </div>

    <div class="flex gap-2 mt-4">
      <Button id="submitButton" class="flex-1" @click="handleSubmit">
        {{ mode === 'new' ? 'Publiser ny hendelse' : 'Oppdater hendelse' }}
      </Button>
      <Button
        v-if="mode === 'edit'"
        class="flex-1 bg-red-600 text-white hover:bg-red-700"
        variant="outline"
        @click="handleDelete"
      >
        Slett hendelse
      </Button>
      <Button id="clearButton" v-if="mode === 'edit'" class="flex-1" variant="outline" @click="eventFormRefresh">
        Avbryt
      </Button>
    </div>
  </Card>
</template>
