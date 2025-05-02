<script setup>
import { ref } from 'vue'
import { Button } from '@/components/ui/button/index.js';
import { Input } from '@/components/ui/input/index.js';
import { Card, CardContent } from '@/components/ui/card/index.js';
import { Label } from '@/components/ui/label/index.js';
import {
  Select,
  SelectItem,
  SelectTrigger,
  SelectContent,
  SelectGroup,
  SelectValue
} from '@/components/ui/select/index.js'

const selectedSeverity = ref(null)
const date = ref(null)
const time = ref(null)

const emit = defineEmits(['submit'])
</script>

<template>
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
      <div class="flex gap-2 items-center">
        <Select v-model="selectedSeverity">
          <SelectTrigger class="w-[180px]">
            <SelectValue placeholder="Velg beredskapsnivå" />
          </SelectTrigger>
          <SelectContent>
            <SelectGroup>
              <SelectItem value="info">Info</SelectItem>
              <SelectItem value="green">Lav</SelectItem>
              <SelectItem value="yellow">Middels</SelectItem>
              <SelectItem value="red">Høy</SelectItem>
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
      <Input class="border w-full" id="event" placeholder="Jordskjelv" />
    </div>

    <div class="flex items-center">
      <Label class="m-2" for="summary">Sammendrag</Label>
      <Input class="border w-full" id="summary" placeholder="Sammendrag av hendelsen" />
    </div>

    <div class="flex align-middle items-center">
      <Label class="m-2" for="start-date">Tidspunkt</Label>
      <div class="flex gap-2 items-center">
        <input type="date" id="start-date" v-model="date" class="p-2 border rounded-md focus:ring-2 focus:ring-blue-500" />
        <input type="time" id="start-time" v-model="time" class="p-2 border rounded-md focus:ring-2 focus:ring-blue-500" />
      </div>
    </div>

    <Button class="flex-1" @click="emit('submit')">Publiser hendelse</Button>
    <Button variant="outline" class="flex-1">Avbryt</Button>
  </Card>
</template>
