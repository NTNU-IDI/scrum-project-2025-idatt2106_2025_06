<script setup>
import { BentoCardCustom } from '@/components/ui/bento/index.js'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert/index.js'
import BentoGrid from '@/components/ui/bento/BentoGrid.vue'

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
        <div class="h-[35rem] overflow-y-auto space-y-2 p-2">
          <div class="space-y-2">
            <template v-for="(alert, index) in alerts" :key="index">
              <Alert class="bg-white">
                <AlertTitle>{{ alert.title }}</AlertTitle>
                <AlertDescription>{{ alert.description }}</AlertDescription>
              </Alert>
            </template>
          </div>
        </div>
      </BentoCardCustom>
    </bento-grid>
  </div>
</template>
<style scoped></style>
