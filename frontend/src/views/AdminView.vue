<script setup>
import { Button } from '@/components/ui/button/index.js'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card/index.js'
import PublishAlert from '@/components/PublishAlert.vue'
import AlertCard from '@/components/AlertCard.vue'

const alerts = [
  {
    id: 1,
    title: 'GODE NYHETER! lalal',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
    time: 'Nå',
    severity: 'red',
  },
  {
    id: 2,
    title: 'Bolle',
    description:
      'Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang.',
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
    title: 'Gå vekk alerts',
    description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
  },
  {
    id: 9,
    title: 'Håp',
    description: 'Håper denne er borte.',
  },
  {
    id: 10,
    title: 'Bø',
    description: 'Borte... bø!.',
  },
  {
    id: 11,
    title: 'GODE NYHETER!',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
  },
  {
    id: 12,
    title: 'Bolle',
    description: 'Gratis bolle på Element.',
  },
  {
    id: 13,
    title: 'Gå vekk alerts',
    description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
  },
  {
    id: 14,
    title: 'Håp',
    description: 'Håper denne er borte.',
  },
  {
    id: 15,
    title: 'Bø',
    description: 'Borte... bø!.',
  },
]

</script>

<template>
  <div class="m-auto flex flex-wrap relative w-full py-10 gap-6 items-stretch mt-10">
    <!-- Redigere og legge til nye infosider om forskjellige scenarioer -->
    <Card class="flex-1 basis-1/4 min-w-[200px] gap-6 max-h-[650px]">
      <CardHeader>
        <CardTitle class="text-2xl">Rediger scenario informasjon</CardTitle>
      </CardHeader>
      <p></p>
      <CardContent class="flex flex-col gap-2 h-full max-h-[calc(100%-80px)]">
        TBA
      </CardContent>
    </Card>

    <!-- Redigere varslinger -->
    <Card class="max-h-[650px] flex-1 basis-1/4 min-w-[300px]">
      <CardHeader class="grid grid-cols-2 items-center w-full">
        <CardTitle class="text-2xl">Varslinger</CardTitle>
        <PublishAlert/>
      </CardHeader>
        <CardContent class="flex flex-col overflow-y-auto max-h-[500px] gap-2">
          <!-- TODO Her hentes det egentlig fra databasen, dette er bare for å vise utseende og at man kan scrolle -->
          <template v-for="(alert, index) in alerts" :key="index">
            <AlertCard
              :description="alert.description"
              :severity="alert.severity"
              :time="alert.time"
              :title="alert.title"
              variant="admin"
            />
          </template>
        </CardContent>
    </Card>

    <!-- Redigere kart -->
    <Card class="max-h-[650px] flex-1 basis-1/4 min-w-[300px]">
      <CardHeader class="grid grid-cols-2 items-center w-full">
        <CardTitle class="text-2xl whitespace-nowrap">Kart og hendelser</CardTitle>
        <router-link class=" justify-self-end" to="/admin/map">
        <Button>Rediger</Button>
        </router-link>
      </CardHeader>
      <CardContent class="h-[80%]">
        <!-- TODO Inne her skal det være hendelser! -->
        <div class="bg-blue-300 flex h-[40%] m-2">

        </div>

        <!-- TODO Inne her skal det være kart! -->
        <div class="bg-blue-200 flex h-[60%] m-2">

        </div>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>

</style>
