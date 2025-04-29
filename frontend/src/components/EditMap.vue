<script setup>

import { Button } from '@/components/ui/button/index.js'
import { Card } from '@/components/ui/card/index.js'
import { Label } from '@/components/ui/label'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs/index.js'
</script>

<template>
  <Tabs default-value="event" class="w-[400px]">
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
          <Input class="border w-full" id="opening-hours" placeholder="Kort innhold av varsling" />
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
</template>

<style scoped>
</style>
