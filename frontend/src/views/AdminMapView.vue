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
      activeTab: 'event'
    };
  },
  components: {
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

        <!-- Adresse-tab -->
        <TabsContent value="event">
          <Card class="flex flex-col gap-2 p-5">
            <div class="flex align-middle">
              <Label class="m-2" for="position">Posisjon</Label>
              <Input class="border w-full" id="position" placeholder="Adresse eller koordinater" />
            </div>
            <div class="flex align-middle">
              <Label class="m-2" for="event">Type</Label>
              <!-- TODO dropdown her istedet? -->
              <Input class="border w-full" id="event" placeholder="Jordskjelv" />
            </div>
            <div class="flex align-middle">
              <Label class="m-2" for="name">Navn</Label>
              <Input class="border w-full" id="name" placeholder="Navn på hendelse" />
            </div>
            <div class="flex items-center">
              <Label class="m-2" for="summary">Sammendrag</Label>
              <Input class="border w-full" id="summary" placeholder="Sammendrag av hendelsen" />
            </div>
            <div class="flex items-center">
              <Label class="m-2" for="description">Beskrivelse</Label>
              <Input class="border w-full" id="description" placeholder="Detaljert beskrivelse av hendelsen" />
            </div>
            <div class="flex align-middle">
              <Label class="m-2" for="severity">Beredskapsnivå</Label>
              <!-- TODO tre valgmuligheter her - grønn, gul eller rød-->
            </div>
            <div class="flex align-middle">
              <Label class="m-2" for="start-date">Tidspunkt</Label>
              <!-- TODO skal komme en kalender/klokke-velger her-->
            </div>
            <div class="flex align-middle">
              <Label class="m-2" for="radius">Radius</Label>
              <!-- TODO slider her, eller bare en tekstboks? -->
            </div>
            <Button class="flex-1">Publiser hendelse</Button>
            <Button variant="destructive" class="flex=1">Slett</Button>
          </Card>
        </TabsContent>

        <!-- Koordinater-tab -->
        <TabsContent value="marker">
          <Card class="flex flex-col gap-2 p-5">
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
              <Label class="m-2" for="name">Navn</Label>
              <Input class="border w-full" id="name" placeholder="Navn på varsel" />
            </div>
            <div class="flex align-middle">
              <Label class="m-2" for="description">Beskrivelse</Label>
              <Input class="border w-full" id="description" placeholder="Kort innhold av varsling" />
            </div>
            <div class="flex align-middle">
              <Label class="m-2" for="opening-hours">Åpningstider</Label>
              <!-- TODO Velge tid her, denne er ikke obligatorisk -->
            </div>
            <div class="flex align-middle">
              <Label class="m-2" for="contact-info">Kontaktinformasjon</Label>
              <Input class="border w-full" id="contact-info" placeholder="" />
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
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>En bombe er sluppet på sluppen, alle eksamener avlyst.</AlertDescription></Alert>
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>Gratis bolle på Element.</AlertDescription></Alert>
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>Systemet er under vedlikehold. Vær tålmodig.</AlertDescription></Alert>
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>Brannøvelse pågår. Ikke vær redd om alarmen går.</AlertDescription></Alert>
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>Vær forsiktig på vei hjem. Snø og glatte veier forventes.</AlertDescription></Alert>
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>Husk å sende inn oppgaven innen fredag kl. 23:59.</AlertDescription></Alert>
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>Viktige endringer i åpningstidene. Se nettsiden for detaljer.</AlertDescription></Alert>
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>Høy temperatur i kontorlokalene. Vennligst ta nødvendige forholdsregler.</AlertDescription></Alert>
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>Det er mulig å hente billetter til neste event i resepsjonen fra kl. 10:00.</AlertDescription></Alert>
          <Alert variant="default"><AlertTitle>Hendelse: </AlertTitle><AlertDescription>Feil på serveren. Vi jobber med å løse problemet så raskt som mulig.</AlertDescription></Alert>
        </CardContent>
      </Card>

      <!-- TODO Kartet -->
      <div class="w-full h-full rounded bg-blue-200"></div>
    </div>
  </div>
</template>

<style scoped>
</style>
