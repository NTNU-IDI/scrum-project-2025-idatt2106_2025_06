<script setup>
import { BentoCardCustom } from '@/components/ui/bento/index.js'
import BentoGrid from '@/components/ui/bento/BentoGrid.vue'
import EventCard from '@/components/EventCard.vue'

const alerts = [
  {
    title: 'Info:',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
  },
  {
    title: 'Info:',
    description: 'Gratis bolle på Element.',
  },
  {
    title: 'Info:',
    description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
  },
  {
    title: 'Info:',
    description: 'Håper denne er borte.',
  },
  {
    title: 'Info:',
    description: 'Borte... bø!.',
  },
  {
    title: 'Info:',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
  },
  {
    title: 'Info:',
    description: 'Gratis bolle på Element.',
  },
  {
    title: 'Info:',
    description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
  },
  {
    title: 'Info:',
    description: 'Håper denne er borte.',
  },
  {
    title: 'Info:',
    description: 'Borte... bø!.',
  },
  {
    title: 'Info:',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
  },
  {
    title: 'Info:',
    description: 'Gratis bolle på Element.',
  },
  {
    title: 'Info:',
    description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
  },
  {
    title: 'Info:',
    description: 'Håper denne er borte.',
  },
  {
    title: 'Info:',
    description: 'Borte... bø!.',
  },
]
</script>

<template>
  <!-- TODO Legge til kart som kan kobles til relaterte alerts?-->
  <!-- TODO Legge til lucide-vue-next så vi kan ha enkle etiketter til varslingene-->
  <div class="m-auto mt-20">
    <bento-grid>
      <BentoCardCustom
        customClass="col-span-4 min-h-[40rem] group hover:group-hover:bg-transparent"
        name="Varslinger"
      >
        <div class="h-full overflow-y-auto space-y-2 p-2">
          <template v-for="(alert, index) in alerts" :key="index">
            <router-link :to="{ name: 'alert', params: { id: index } }" class="pointer-events-auto">
              <EventCard title="" />
            </router-link>
          </template>
        </div>
      </BentoCardCustom>
    </bento-grid>
  </div>
</template>
<style scoped></style>
