<script setup>
import {ref, defineProps, onMounted} from 'vue'
import { watch } from 'vue'
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

onMounted(() => {
  const currentDate = new Date();
  const formattedDate = currentDate.toISOString().split('T')[0]; // Formater datoen som YYYY-MM-DD
  const formattedTime = currentDate.toTimeString().split(' ')[0].slice(0, 5); // Formater tiden som HH:MM

  date.value = formattedDate;
  time.value = formattedTime;
});

const props = defineProps({
  token: {
    type: String,
    required: true,
  },
  mode: {
    type: String,
    default: 'new', //new or edit
  },
  eventId: [String, Number],
});

const id = ref(null);
const title = ref('');
const content = ref('');
const position = ref('');
const radius = ref(null);
const eventType = ref('other');
const eventStatus = ref('ongoing')
const description = ref('');
const selectedSeverity = ref('info');
const date = ref(null);
const time = ref(null);

const eventFormRefresh = () => {
  title.value = '';
  content.value = '';
  description.value = '';
  position.value = '';
  radius.value = null;
  eventType.value = 'other';
  eventStatus.value = 'ongoing';
  selectedSeverity.value = 'info'
  date.value = null;
  time.value = null;
};

defineExpose({
  eventFormRefresh,
});

async function handleSubmit() {
  console.log('Handling submit for mode:', props.mode);

  const eventPayload = {
    name: title.value,
    content: content.value,
    description: description.value,
    status: eventStatus.value,
    position: position.value,
    impact_area_radius_km: radius.value,
    type: eventType.value,
    severity: selectedSeverity.value,
    startDate: date.value && time.value ? date.value + 'T' + time.value : null,
  }

  try {
    const token = props.token;
    if (props.mode === 'new') {
      await eventStore.createNewEvent(eventPayload, token);
    } else if (props.mode === 'edit') {
      const eventId = id.value;
      await eventStore.updateExistingEvent(eventId, eventPayload, token);
    }
  } catch (error) {
    console.error("Feil ved innsending av hendelse:", error);
  }
}

watch(() => props.eventId, async (newId) => {
  if (newId) {
    console.log('Fyller skjema for eventId:', newId);
    console.log('Fyller skjema med token:', props.token);
    eventFormRefresh()

    id.value = newId;

    const eventData = await eventStore.getEventById(newId, props.token);
    if (eventData) {
      title.value = eventData.name;
      description.value = eventData.content;
      console.log(eventData.name);
      console.log(eventData.content);
      /**position.value = eventData.position;
      radius.value = eventData.impact_area_radius_km;
      eventType.value = eventData.type;
      description.value = eventData.description;
      selectedSeverity.value = eventData.severity;
      if (eventData.start_date) {
        const [datePart, timePart] = eventData.start_date.split('T');
        date.value = datePart;
        time.value = timePart;
      }*/
    }
  }
});
</script>


<template>
  <Card class="flex flex-col gap-2 p-5">
    <div class="flex items-center">
      <Label class="m-2" for="title">Tittel</Label>
      <Input class="border w-full" id="title" placeholder="Navn på hendelse" v-model="title" />
    </div>

    <div class="flex items-center">
      <Label class="m-2" for="description">Beskrivelse</Label>
      <Input class="border w-full" id="description" placeholder="Detaljert beskrivelse av hendelsen" v-model="content" />
    </div>

    <div class="flex align-middle items-center">
      <Label class="m-2" for="severity">Beredskapsnivå</Label>
      <div class="flex gap-2 items-center">
        <Select v-model="selectedSeverity">
          <SelectTrigger class="w-[180px]">
            <SelectValue placeholder="Velg beredskapsnivå" />
          </SelectTrigger>
          <SelectContent>
              <SelectItem value="info" severity="info">Info</SelectItem>
              <SelectItem value="green" severity="low">Lav</SelectItem>
              <SelectItem value="yellow" severity="medium">Middels</SelectItem>
              <SelectItem value="red" severity="high">Høy</SelectItem>
          </SelectContent>
        </Select>
      </div>
    </div>

    <div class="flex align-middle items-center">
      <Label class="m-2" for="event">Hendelse</Label>
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

    <div class="flex align-middle items-center">
      <Label class="m-2" for="event">Hendelse</Label>
      <div class="flex gap-2 items-center">
        <Select v-model="eventStatus">
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
      <Label class="m-2" for="position">Posisjon</Label>
      <Input class="border w-full" id="position" placeholder="Adresse eller koordinater" v-model="position" />
    </div>

    <div class="flex items-center">
      <Label class="m-2" for="radius">Radius</Label>
      <input type="number" step="10" class="h-9 rounded-md border border-input max-w-[100px] text-right" id="radius" placeholder="1000" v-model.number="radius" />
      <p class="ml-2">meter</p>
    </div>


    <div class="flex items-center">
      <Label class="m-2" for="summary">Sammendrag</Label>
      <Input class="border w-full" id="summary" placeholder="Sammendrag av hendelsen" v-model="description" />
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
