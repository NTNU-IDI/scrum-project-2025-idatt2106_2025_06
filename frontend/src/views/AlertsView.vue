<script setup>
import AlertCard from '@/components/AlertCard.vue'
import EventCard from '@/components/EventCard.vue'

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

const events = [
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
]
</script>

<template>
  <div class="m-auto mt-2 h-full w-full">
    <div
      class="flex z-10 w-full gap-2 py-4 rounded-lg overflow-x-scroll scroll-smooth scrollbar-hide absolute max-w-6xl"
    >
      <template v-for="(alert, index) in alerts" :key="index">
        <RouterLink :to="'alerts/' + alert.id" class="relative">
          <AlertCard
            :description="alert.description"
            :severity="alert.severity"
            :time="alert.time"
            :title="alert.title"
            variant="expand"
          />
        </RouterLink>
      </template>
    </div>

    <div class="mt-24">
      <div class="flex flex-col gap-2">
        <div class="grid w-full gap-2 [grid-template-columns:repeat(auto-fit,minmax(20rem,1fr))]">
          <template v-for="(event, index) in events" :key="index">
            <RouterLink :to="'events/' + event.id" class="relative">
              <EventCard
                :description="event.description"
                :severity="event.severity"
                :time="event.time"
                :title="event.title"
                variant="expand"
              />
            </RouterLink>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
