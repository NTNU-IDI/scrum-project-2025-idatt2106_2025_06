<script setup>

import { Button } from '@/components/ui/button/index.js'
import { Card, CardDescription } from '@/components/ui/card/index.js'
</script>

<template>
  <Card class="flex flex-col gap-2 p-5">
    <CardDescription>Rediger hendelse</CardDescription>
    <div class="flex align-middle">
      <Label class="m-2" for="address">Adresse</Label>
      <Input class="border w-full" id="address" placeholder="Trondheim" />
    </div>
    <div class="flex align-middle">
      <Label class="m-2" for="event">Hendelse</Label>
      <Input class="border w-full" id="event" placeholder="Jordskjelv" />
    </div>
    <div class="flex align-middle">
      <Label class="m-2" for="description">Beskrivelse</Label>
      <Input class="border w-full" id="description" placeholder="En beskrivelse av hendelsen" />
    </div>
    <div class="flex align-middle">
      <Label class="m-2" for="severity">Beredskapsnivå</Label>
      <!--dropdown menu her-->
    </div>
    <div class="flex align-middle">
      <Label class="m-2" for="start-date">Tidspunkt</Label>
      <!--skal komme en kalender/klokke-velger her-->
    </div>
    <div class="flex gap-2">
      <Button class="flex-1">Avbryt</Button>
      <Button class="flex-1">Publiser</Button>
    </div>
    <Button variant="destructive" class="flex=1">Slett hendlese</Button>
  </Card>
</template>

<style scoped>

</style>
