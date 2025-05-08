<script setup>
import { ref, defineProps, watch } from 'vue'
import { useEventStore } from '@/stores/event.js'
import { Card} from '@/components/ui/card/index.js';
import {Button} from '@/components/ui/button';
import {Input} from '@/components/ui/input';
import {Label} from '@/components/ui/label';
import {
  Select,
  SelectItem,
  SelectTrigger,
  SelectContent,
  SelectValue
} from '@/components/ui/select/index.js'

const eventStore = useEventStore()

const props = defineProps({
  token: {
    type: String,
    required: true,
  },
  mode: {
    type: String,
    default: 'new', //new or edit
  },
  eventData: Object,
});

const mode = ref(props.mode);
const id = ref(null)
const title = ref('')
const description = ref('')
const content = ref('')
const latitude = ref('')
const longitude = ref('')
const radius = ref(1)
const eventType = ref('other')
const eventStatus = ref('ongoing')
const selectedSeverity = ref('low')

const eventFormRefresh = () => {
  id.value = ''
  title.value = ''
  content.value = ''
  description.value = ''
  latitude.value = ''
  longitude.value = ''
  radius.value = 1
  eventType.value = 'other'
  eventStatus.value = 'ongoing'
  selectedSeverity.value = 'low'
};

defineExpose({
  eventFormRefresh,
});

const errors = ref({
  title: false,
  description: false,
  latitude: false,
  longitude: false,
  severity: false,
  eventType: false,
});

const validateForm = () => {
  errors.value = {
    title: false,
    description: false,
    latitude: false,
    longitude: false,
    severity: false,
    eventType: false,
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

  if ((latitude.value.trim() || longitude.value.trim()) &&
    (!latitude.value.trim() || !longitude.value.trim() || isNaN(parseFloat(latitude.value)) || isNaN(parseFloat(longitude.value)))) {
    errors.value.latitude = true;
    errors.value.longitude = true;
    hasError = true;
  }


  if (!selectedSeverity.value) {
    errors.value.severity = true;
    hasError = true;
  }

  if (!eventType.value) {
    errors.value.eventType = true;
    hasError = true;
  }

  return hasError;
};



async function handleSubmit() {
  const hasError = validateForm();
  if (hasError) {
    return;
  }

  console.log('Handling submit for mode:', props.mode)
  const now = new Date().toISOString();

  if (!title.value || !description.value || !selectedSeverity.value || !eventType.value) {
    alert('Fyll ut alle obligatoriske felter: Tittel, Beskrivelse, Beredskapsnivå og Hendelse.');
    return;
  }

  const eventPayload = {
    name: title.value,
    description: description.value,
    content: content.value,
    status: eventStatus.value,
    location: {
      latitude: latitude.value,
      longitude: longitude.value
    },

    impact_area_radius_km: radius.value,
    type: eventType.value,
    severity: selectedSeverity.value,
    startTime: now,
    endTime: now,
  }
  console.log("Event Payload:", eventPayload);

  try {
    const token = props.token
    if (props.mode === 'new') {
      console.log('EventCard props:', props.eventData);
      await eventStore.createNewEvent(eventPayload, token)
    } else if (props.mode === 'edit') {
      const eventId = id.value
      console.log('Updating event id:', id.value)
      await eventStore.updateExistingEvent(eventId, eventPayload, token)
    }
  } catch (error) {
    console.error("Feil ved innsending av hendelse:", error)
  }
}

//If an EventCard button is pressed
watch(() => props.eventData, async (newData) => {
  if (newData) {
    mode.value = 'edit'
    console.log('Fyller skjema for:', props.mode)

    id.value = newData.eventId
    title.value = newData.name
    content.value = newData.content
    latitude.value = newData.location.latitude
    eventType.value = newData.type
    longitude.value = newData.location.longitude
    description.value = newData.description
    eventStatus.value = newData.status
    selectedSeverity.value = newData.severity
  }
});
</script>

<template>
  <Card class="flex flex-col gap-2 p-5">
    <div class="flex items-center">
      <Label class="m-2" for="title">Tittel</Label>
      <Input
        :class="['border w-full', errors.title ? 'border-red-500' : '']"
        id="title"
        placeholder="Navn på hendelse"
        v-model="title"
      />
    </div>

    <Input
      :class="['border w-full', errors.description ? 'border-red-500' : '']"
      id="description"
      placeholder="Kort beskrivelse av hendelsen"
      v-model="description"
    />

    <div class="flex align-middle items-center">
      <Label class="m-2" for="severity">Beredskapsnivå</Label>
      <div class="flex gap-2 items-center">
        <Select
          v-model="selectedSeverity"
          :class="errors.severity ? 'border-red-500' : ''">
          <SelectTrigger class="w-[180px]">
            <SelectValue placeholder="Velg beredskapsnivå" />
          </SelectTrigger>
          <SelectContent>
              <SelectItem value="low" severity="low">Lav</SelectItem>
              <SelectItem value="medium" severity="medium">Middels</SelectItem>
              <SelectItem value="high" severity="high">Høy</SelectItem>
          </SelectContent>
        </Select>
      </div>
    </div>

    <div class="flex align-middle items-center">
      <Label class="m-2" for="eventStatus">Status</Label>
      <div class="flex gap-2 items-center">
        <Select v-model="eventStatus">
          <SelectTrigger class="w-[180px]">
            <SelectValue placeholder="Velg type hendelse" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="planned">Planlagt</SelectItem>
            <SelectItem value="ongoing">Pågår</SelectItem>
            <SelectItem value="finished">Ferdig</SelectItem>
            <SelectItem value="cancelled">Avbrutt</SelectItem>
          </SelectContent>
        </Select>
      </div>
    </div>

    <div class="flex align-middle items-center">
      <Label class="m-2" for="eventType">Hendelse</Label>
      <div class="flex gap-2 items-center">
        <Select
          v-model="eventType"
          :class="errors.eventType ? 'border-red-500' : ''">
          <SelectTrigger class="w-[180px]">
            <SelectValue placeholder="Velg type hendelse" />
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

    <div class="flex items-center">
      <Label class="m-2" for="latitude">Posisjon</Label>
      <div class="flex flex-col ">
        <Label class="m-2" for="latitude">Breddegrad</Label>
        <Input
          :class="['border w-full', errors.latitude ? 'border-red-500' : '']"
          id="latitude"
          placeholder="eks: 64.232321"
          v-model="latitude"
        />
      </div>

      <div class="flex flex-col ">
        <Label class="m-2" for="longitude">Lengdegrad</Label>
        <Input
          :class="['border w-full', errors.longitude ? 'border-red-500' : '']"
          id="longitude"
          placeholder="eks: 10.422132"
          v-model="longitude"
        />
      </div>
    </div>

    <div class="flex items-center">
      <Label class="m-2" for="radius">Radius</Label>
      <input type="number" step="10" class="h-9 rounded-md border border-input max-w-[100px] text-right" id="radius" placeholder="1000" v-model.number="radius" />
      <p class="ml-2">km</p>
    </div>

    <div class="flex items-center">
      <Label class="m-2" for="content">Mer info</Label>
      <Input class="border w-full" id="content" placeholder="Lengre beskrivelse av hendelsen" v-model="content" />
    </div>

    <Button class="flex-1" @click="handleSubmit">
      {{ props.mode === 'new' ? 'Publiser ny hendelse' : 'Oppdater hendelse' }}
    </Button>

    <Button variant="outline" class="flex-1">Avbryt</Button>
  </Card>
</template>
