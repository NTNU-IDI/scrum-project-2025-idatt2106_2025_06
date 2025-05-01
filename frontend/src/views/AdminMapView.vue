<script>
import { defineComponent } from "vue";
import Map from "@/components/Map.vue";
import SearchAddressCoordinates from '@/components/SearchAddressCoordinates.vue';
import { Button } from '@/components/ui/button/index.js';
import { Input } from '@/components/ui/input/index.js'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card/index.js'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert/index.js'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs/index.js'
import { Label } from '@/components/ui/label/index.js'
import EventCard from '@/components/EventCard.vue'
import {
  Select,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectContent,
  SelectGroup,
  SelectValue
} from '@/components/ui/select/index.js'

export default defineComponent({
  data() {
    return {
      location: {
        lng: 10.40574,
        lat: 63.41754,
        bearing: 0,
        pitch: 0,
        zoom: 12
      },
      showSearchPopup: true,
      activeTab: 'event',
      events: [
        {
          id: 1,
          title: 'GODE NYHETER! lalal',
          description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
          time: 'Nå',
          severity: 'info',
        },
        {
          id: 2,
          title: 'Bolle',
          description:
            'Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang.Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang.Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang.',
          time: 'Nå',
          severity: 'red',
        },
        {
          id: 3,
          title: 'Gå vekk alerts',
          description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
          time: '11:42',
          severity: 'yellow',
        },
        {
          id: 4,
          title: 'Håp',
          description: 'Håper denne er borte.',
          time: '11:42',
          severity: 'green',
        },
        {
          id: 5,
          title: 'Bø',
          description: 'Borte... bø!.',
        },
        {
          id: 6,
          title: 'GODE NYHETER!',
          description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
        },
        {
          id: 7,
          title: 'Bolle',
          description: 'Gratis bolle på Element.',
        },
        {
          id: 8,
          title: 'Kanel',
          description: 'Gratis bolle på Element.',
        },
        {
          id: 9,
          title: 'Snurr',
          description: 'Gratis bolle på Element.',
        },
      ]
    };
  },
  components: {
    SelectValue,
    SelectGroup,
    SelectContent,
    SelectTrigger,
    SelectItem,
    SelectLabel,
    Select,
    EventCard,
    TabsTrigger,
    TabsList, Label,
    TabsContent, Tabs,
    AlertDescription,
    AlertTitle, Alert,
    Card,
    CardHeader,
    CardTitle, CardContent,
    Input,
    Button,
    SearchAddressCoordinates,
    Map
  }
});
</script>

<template>
  <div class="m-auto mt-10 flex flex-1 w-full py-10 gap-6">
    <RouterLink  class="absolute z-10 top-20 left-30" to="/admin">
      <Button>Tilbake</Button>
    </RouterLink>

    <!-- Venstre kolonne -->
    <div class="flex flex-col">
      <Tabs v-model="activeTab" class="w-[400px]">
        <TabsList class="grid w-full grid-cols-2">
          <TabsTrigger value="event">
            Hendelse
          </TabsTrigger>
          <TabsTrigger value="marker">
            Markør
          </TabsTrigger>
        </TabsList>

        <!-- Hendelse-tab -->
        <TabsContent value="event">
          <Card class="flex flex-col gap-2 p-5">
            <div class="flex items-center">
              <Label class="m-2" for="title">Tittel</Label>
              <Input class="border w-full" id="title" placeholder="Navn på hendelse" />
            </div>

            <div class="flex items-center">
              <Label class="m-2" for="description">Beskrivelse</Label>
              <Input class="border w-full" id="description" placeholder="Detaljert beskrivelse av hendelsen" />
            </div>

            <div class="flex align-middle items-center">
              <Label class="m-2" for="severity">Beredskapsnivå</Label>
              <!-- Dropdown for valg av beredskapsnivå -->
              <div class="flex gap-2 items-center">
                <Select v-model="selectedSeverity">
                  <SelectTrigger class="w-[180px]">
                    <SelectValue placeholder="Velg beredskapsnivå" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem value="info" severity="info">Info</SelectItem>
                      <SelectItem value="green" severity="green">Lav</SelectItem>
                      <SelectItem value="yellow" severity="yellow">Middels</SelectItem>
                      <SelectItem value="red" severity="red">Høy</SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>
            </div>

            <div class="flex items-center">
              <Label class="m-2" for="position">Posisjon</Label>
              <Input class="border w-full" id="position" placeholder="Adresse eller koordinater" />
            </div>

            <div class="flex items-center">
              <Label class="m-2" for="radius">Radius</Label>
              <input type="number" step="10" class="h-9 rounded-md border border-input max-w-[100px] text-right" id="radius" placeholder="1000" />
              <p class="ml-2">meter</p>
            </div>

            <div class="flex items-center">
              <Label class="m-2" for="event">Type</Label>
              <!-- TODO dropdown her istedet? -->
              <Input class="border w-full" id="event" placeholder="Jordskjelv" />
            </div>

            <div class="flex items-center">
              <Label class="m-2" for="summary">Sammendrag</Label>
              <Input class="border w-full" id="summary" placeholder="Sammendrag av hendelsen" />
            </div>

            <div class="flex align-middle items-center">
              <Label class="m-2" for="start-date">Tidspunkt</Label>

              <div class="flex gap-2 items-center">
                <!-- Kalender for dato -->
                <input
                  type="date"
                  id="start-date"
                  v-model="date"
                  class="p-2 border rounded-md focus:ring-2 focus:ring-blue-500"
                />
                <!-- Klokke for tid -->
                <input
                  type="time"
                  id="start-time"
                  v-model="time"
                  class="p-2 border rounded-md focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>
            <Button class="flex-1">Publiser hendelse</Button>
            <Button variant="destructive" class="flex=1">Slett</Button>
          </Card>
        </TabsContent>

        <!-- Markør-tab -->
        <TabsContent value="marker">
          <Card class="flex flex-col gap-2 p-5">
            <div class="flex align-middle">
              <Label class="m-2" for="title">Tittel</Label>
              <Input class="border w-full" id="title" placeholder="Navn på markør" />
            </div>

            <div class="flex align-middle">
              <Label class="m-2" for="position">Posisjon</Label>
              <Input class="border w-full" id="position" placeholder="Posisjon til markør" />
            </div>

            <div class="flex align-middle">
              <Label class="m-2" for="type">Type</Label>
              <!-- TODO DropBox her -->
              <Input class="border w-full" id="type" placeholder="Type markør" />
            </div>

            <div class="flex align-middle">
              <Label class="m-2" for="description">Beskrivelse</Label>
              <Input class="border w-full" id="description" placeholder="Kort innhold av varsling" />
            </div>

            <div class="flex align-middle">
              <Label class="m-2" for="opening-hours">Åpningstider</Label>
              <Input class="border w-full" id="opening-hours" placeholder="eks 08:00-16:00" />
            </div>

            <div class="flex align-middle">
              <Label class="m-2" for="contact-info">Kontaktinformasjon</Label>
              <Input class="border w-full" id="contact-info" placeholder="tlf eller email" />
            </div>

            <Button class="flex-1">Plasser markør</Button>
            <Button variant="destructive" class="flex=1">Slett</Button>
          </Card>
        </TabsContent>
      </Tabs>
    </div>

    <!-- Kart og popup -->
    <div class="flex relative flex-[2] h-[600px]">
      <SearchAddressCoordinates class="absolute top-4 left-4 z-20"
      />

      <Card v-if="activeTab === 'event'" class="h-[100%] absolute top-0 right-0 z-30">
        <CardHeader>
          <CardTitle class="text-2xl">Hendelser</CardTitle>
        </CardHeader>
        <CardContent class="max-w-[350px] max-h-[85%] overflow-y-auto flex flex-col gap-2">
          <!-- TODO Her hentes det egentlig fra databasen, dette er bare for å vise utseende og at man kan scrolle -->
          <template v-for="(event, index) in events" :key="index">
              <EventCard
                :description="event.description"
                :severity="event.severity"
                :time="event.time"
                :title="event.title"
                variant="admin"
              />
          </template>
        </CardContent>
      </Card>

      <!-- TODO Kartet -->
      <div class="w-full h-full rounded bg-blue-200"></div>
    </div>
  </div>
</template>

<style scoped>
</style>
