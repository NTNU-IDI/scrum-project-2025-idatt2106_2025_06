<script setup>
import { Button } from '@/components/ui/button/index.js'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card/index.js'
import PublishAlert from '@/components/PublishAlert.vue'
import AlertCard from '@/components/AlertCard.vue'
import { ref } from 'vue'

const alerts = ref([
  {
    id: 1,
    title: 'GODE NYHETER! lalal',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
    date: '2025-05-01',
    time: '12:00',
    severity: 'red',
  },
  {
    id: 2,
    title: 'Bolle',
    description: 'Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang.',
    date: '2025-05-01',
    time: '13:00',
    severity: 'red',
  },
  {
    id: 3,
    title: 'Gå vekk alerts',
    description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
    date: '2025-05-01',
    time: '14:30',
    severity: 'yellow',
  },
  {
    id: 4,
    title: 'Håp',
    description: 'Håper denne er borte.',
    date: '2025-05-01',
    time: '11:42',
    severity: 'green',
  },
  {
    id: 5,
    title: 'Bø',
    description: 'Borte... bø!.',
    date: '2025-05-01',
    time: '14:30',
  },
  {
    id: 6,
    title: 'GODE NYHETER!',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
    date: '2025-05-02',
    time: '10:00',
  },
  {
    id: 7,
    title: 'Bolle',
    description: 'Gratis bolle på Element.',
    date: '2025-05-21',
    time: '14:30',
  },
  {
    id: 8,
    title: 'Gå vekk alerts',
    description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
    date: '2025-05-03',
    time: '09:15',
  },
  {
    id: 9,
    title: 'Håp',
    description: 'Håper denne er borte.',
    date: '2025-05-04',
    time: '08:45',
  },
  {
    id: 10,
    title: 'Bø',
    description: 'Borte... bø!.',
    date: '2025-05-05',
    time: '16:30',
  },
  {
    id: 11,
    title: 'GODE NYHETER!',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
    date: '2025-05-06',
    time: '15:00',
  },
  {
    id: 12,
    title: 'Bolle',
    description: 'Gratis bolle på Element.',
    date: '2025-05-07',
    time: '12:30',
  },
  {
    id: 13,
    title: 'Gå vekk alerts',
    description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
    date: '2025-05-08',
    time: '11:00',
  },
  {
    id: 14,
    title: 'Håp',
    description: 'Håper denne er borte.',
    date: '2025-05-09',
    time: '17:45',
  },
  {
    id: 15,
    title: 'Bø',
    description: 'Borte... bø!.',
    date: '2025-05-10',
    time: '18:15',
  },
]);

</script>

<template>
  <div class="m-auto flex flex-wrap relative w-full py-10 gap-6 items-stretch mt-10">
    <Card class="flex-1 basis-1/4 min-w-[200px] gap-6 max-h-[650px]">
      <CardHeader>
        <CardTitle class="text-2xl">Rediger scenario informasjon</CardTitle>
      </CardHeader>
      <CardContent class="flex flex-col gap-2 h-full max-h-[calc(100%-80px)]">
        TBA
      </CardContent>
    </Card>

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
              :date="alert.date"
              :time="alert.time"
              :title="alert.title"
              variant="admin"
            />
          </template>
        </CardContent>
    </Card>

    <Card class="max-h-[650px] flex-1 basis-1/4 min-w-[300px]">
      <CardHeader class="grid grid-cols-2 items-center w-full">
        <CardTitle class="text-2xl whitespace-nowrap">Kart og hendelser</CardTitle>
        <router-link class=" justify-self-end" to="/admin/map">
        <Button>Rediger</Button>
        </router-link>
      </CardHeader>
      <CardContent class="h-[85%]">
        <!-- TODO Inne her skal det være kart! -->
        <div class="bg-blue-200 flex h-[100%] m-2">

        </div>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>

</style>
