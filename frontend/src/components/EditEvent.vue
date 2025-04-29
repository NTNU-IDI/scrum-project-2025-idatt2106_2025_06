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
      <TabsTrigger value="post">
        Innlegg
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
          <!--tre valgmuligheter her - grønn, gul eller rød-->
        </div>
        <div class="flex align-middle">
          <Label class="m-2" for="start-date">Tidspunkt</Label>
          <!--skal komme en kalender/klokke-velger her-->
        </div>
        <div class="flex align-middle">
          <Label class="m-2" for="radius">Radius</Label>
          <!--slider her, eller bare en tekstboks? -->
        </div>
        <Button class="flex-1">Publiser</Button>
        <Button variant="destructive" class="flex=1">Slett hendlese</Button>
      </Card>
    </TabsContent>

    <!-- Koordinater-tab -->
    <TabsContent value="post">
      <Card class="flex flex-col gap-2 p-5">
        <div class="flex align-middle">
          <Label class="m-2" for="name">Navn</Label>
          <Input class="border w-full" id="name" placeholder="Navn på varsel" />
        </div>
        <div class="flex align-middle">
          <Label class="m-2" for="description">Beskrivelse</Label>
          <Input class="border w-full" id="description" placeholder="Kort innhold av varsling" />
        </div>
        <div class="flex align-middle">
          <Label class="m-2" for="content">Innhold</Label>
          <!-- TODO Kanskje legge til en større type tekstboks her -->
          <Input class="border w-full" id="content" placeholder="Næremere beskrivelse av varsling" />
        </div>
        <Button class="flex-1">Publiser</Button>
        <Button variant="destructive" class="flex=1">Slett hendlese</Button>
      </Card>
    </TabsContent>
  </Tabs>
</template>

<style scoped>
</style>
