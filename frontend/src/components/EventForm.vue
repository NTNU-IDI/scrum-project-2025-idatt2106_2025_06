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
const radius = ref(null)
const eventType = ref('other')
const eventStatus = ref('ongoing')
const selectedSeverity = ref('low')
const date = ref()
const time = ref()

const eventFormRefresh = () => {
  id.value = ''
  title.value = ''
  content.value = ''
  description.value = ''
  latitude.value = ''
  longitude.value = ''
  radius.value = null
  eventType.value = 'other'
  eventStatus.value = 'ongoing'
  selectedSeverity.value = 'low'
  time.value = ''
  date.value = ''
};

defineExpose({
  eventFormRefresh,
});

async function handleSubmit() {
  console.log('Handling submit for mode:', props.mode)

  if (!title.value || !description.value || !selectedSeverity.value || !eventType.value) {
    alert('Fyll ut alle obligatoriske felter: Tittel, Beskrivelse, Beredskapsnivå og Hendelse.');
    return;
  }

  let datetime = date.value && time.value ? `${date.value}T${time.value}` : null
  console.log('sjekk', datetime)

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
    startDate: datetime ? new Date(datetime).toISOString() : null,
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
    description.value = newData.description
    content.value = newData.content
    eventType.value = newData.type
    eventStatus.value = newData.status
    selectedSeverity.value = newData.severity
    latitude.value = newData.location.latitude
    longitude.value = newData.location.longitude

    if (newData.startDate) {
      console.log('ny data dato', newData.startDate)
      const [datePart, timePart] = newData.startDate.split('T')
      date.value = datePart
      time.value = timePart?.slice(0,5) ?? '00:00'
    }
  }
});
</script>

<template>
  <Card class="flex flex-col gap-2 p-5">
    <div class="flex items-center">
      <Label class="m-2" for="title">Tittel</Label>
      <Input
        :class="['border w-full', !title ? 'border-blue-500' : '']"
        id="title"
        placeholder="Navn på hendelse"
        v-model="title"
      />

    </div>

    <div class="flex items-center">
      <Label class="m-2" for="description">Beskrivelse</Label>
      <Input class="border w-full" id="description" placeholder="Kort beskrivelse av hendelsen" v-model="description" />
    </div>

    <div class="flex align-middle items-center">
      <Label class="m-2" for="severity">Beredskapsnivå</Label>
      <div class="flex gap-2 items-center">
        <Select v-model="selectedSeverity">
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
        <Select v-model="eventType">
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
      <Label class="m-2" for="latitude">Breddegrad</Label>
      <div class="flex flex-col ">
        <Label class="m-2" for="latitude">Latitude</Label>
        <Input class="border w-full" id="latitude" placeholder="eks: 64.232321" v-model="latitude" />
      </div>

      <div class="flex flex-col ">
        <Label class="m-2" for="longitude">Lengdegrad</Label>
        <Input class="border w-full" id="longitude" placeholder="eks: 10.422132" v-model="longitude" />
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

    <div class="flex align-middle items-center">
      <Label class="m-2" for="start-date">Tidspunkt</Label>
      <div class="flex gap-2 items-center">
        <input type="date" id="start-date" v-model="date" class="p-2 border rounded-md focus:ring-2 focus:ring-blue-500" />
        <input type="time" id="start-time" v-model="time" class="p-2 border rounded-md focus:ring-2 focus:ring-blue-500" />
      </div>
    </div>

    <Button class="flex-1" @click="handleSubmit">
      {{ props.mode === 'new' ? 'Publiser ny hendelse' : 'Oppdater hendelse' }}
    </Button>

    <Button variant="outline" class="flex-1">Avbryt</Button>
  </Card>
</template>
