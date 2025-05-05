<script setup>
import { ref } from 'vue';
import { Button } from '@/components/ui/button/index.js';
import { Input } from '@/components/ui/input/index.js';
import { Card } from '@/components/ui/card/index.js';
import { Label } from '@/components/ui/label/index.js';

const emit = defineEmits(['submit'])

const title = ref('')
const position = ref('')
const type = ref('')
const description = ref('')
const openingHours = ref('')
const contactInfo = ref('')
</script>

<template>
  <Card class="flex flex-col gap-2 p-5">
    <div class="flex align-middle">
      <Label class="m-2" for="title">Tittel</Label>
      <Input v-model="title" class="border w-full" id="title" placeholder="Navn på markør" />
    </div>

    <div class="flex align-middle">
      <Label class="m-2" for="position">Posisjon</Label>
      <Input v-model="position" class="border w-full" id="position" placeholder="Posisjon til markør" />
    </div>

    <div class="flex align-middle">
      <Label class="m-2" for="type">Type</Label>
      <Input v-model="type" class="border w-full" id="type" placeholder="Type markør" />
    </div>

    <div class="flex align-middle">
      <Label class="m-2" for="description">Beskrivelse</Label>
      <Input v-model="description" class="border w-full" id="description" placeholder="Kort innhold av varsling" />
    </div>

    <div class="flex align-middle">
      <Label class="m-2" for="opening-hours">Åpningstider</Label>
      <Input v-model="openingHours" class="border w-full" id="opening-hours" placeholder="Eks 08:00-16:00" />
    </div>

    <div class="flex align-middle">
      <Label class="m-2" for="contact-info">Kontaktinformasjon</Label>
      <Input v-model="contactInfo" class="border w-full" id="contact-info" placeholder="Tlf eller e-post" />
    </div>

    <Button class="flex-1" @click="emit('submit')">Plasser markør</Button>
    <Button variant="destructive" class="flex-1">Slett</Button>
  </Card>
</template>
