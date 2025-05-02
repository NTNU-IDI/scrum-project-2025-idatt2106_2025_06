<script setup>
import SearchAddressCoordinates from '@/components/SearchAddressCoordinates.vue';
import { Button } from '@/components/ui/button/index.js';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card/index.js';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs/index.js';
import EventCard from '@/components/EventCard.vue';
import EventForm from '@/components/EventForm.vue'
import { ref } from 'vue';
import MarkerForm from '@/components/MarkerForm.vue'

const activeTab = ref('event');

const events = ref([
  { id: 1, title: 'GODE NYHETER! lalal', description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.', date: '2025-05-02', time: '14:30', position: 'Sluppen, Trondheim', severity: 'info' },
  { id: 2, title: 'Bolle', description: 'Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang.', date: '2025-05-02', time: '09:00', position: 'Element, Gløshaugen', severity: 'red' },
  { id: 3, title: 'Gå vekk alerts', description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis', date: '2025-05-01', time: '11:42', position: 'Kalvskinnet, Trondheim', severity: 'yellow' },
  { id: 4, title: 'Håp', description: 'Håper denne er borte.', date: '2023-05-01', time: '11:42', position: 'Solstien 4, Trondheim', severity: 'green' },
  { id: 5, title: 'Bø', description: 'Borte... bø!', date: '2025-05-03', time: '16:15', position: 'Festningen, Trondheim', severity: 'info' },
  { id: 6, title: 'GODE NYHETER!', description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.', date: '2025-05-01', time: '14:35', position: 'Sluppen, Trondheim', severity: 'red' },
  { id: 7, title: 'Bolle', description: 'Gratis bolle på Element.', date: '2025-05-02', time: '10:00', position: 'Element, Gløshaugen', severity: 'green' },
  { id: 8, title: 'Kanel', description: 'Gratis bolle på Element.', date: '2025-05-02', time: '10:15', position: 'Element, Gløshaugen', severity: 'yellow' },
  { id: 9, title: 'Snurr', description: 'Gratis bolle på Element.', date: '2025-05-02', time: '10:30', position: 'Element, Gløshaugen', severity: 'info' },
]);
</script>

<template>
  <div class="m-auto mt-10 flex flex-1 w-full py-10 gap-6">
    <RouterLink  class="absolute z-10 top-20 left-30" to="/admin">
      <Button>Tilbake</Button>
    </RouterLink>

    <div class="flex flex-col">
      <Tabs v-model="activeTab" class="w-[400px]">
        <TabsList class="grid w-full grid-cols-2">
          <TabsTrigger value="event">
            Hendelse
          </TabsTrigger>
          <TabsTrigger value="marker">
            Markør
          </TabsTrigger>
        </TabsList>

        <TabsContent value="event">
          <EventForm/>
        </TabsContent>

        <TabsContent value="marker">
          <MarkerForm/>
        </TabsContent>
      </Tabs>
    </div>

    <!-- Kart og popup -->
    <div class="flex relative flex-[2] h-[600px]">
      <SearchAddressCoordinates class="absolute top-4 left-4 z-20"
      />

      <Card v-if="activeTab === 'event'" class="h-[100%] absolute top-0 right-0 z-30">
        <CardHeader class="grid grid-cols-2 items-center w-full">
          <CardTitle class="text-2xl">Hendelser</CardTitle>
          <Button class="justify-self-end">Ny hendelse</Button>
        </CardHeader>
        <CardContent class="max-w-[350px] max-h-[85%] overflow-y-auto flex flex-col gap-2">
          <template v-for="(event, index) in events" :key="index">
              <EventCard
                :description="event.description"
                :severity="event.severity"
                :time="event.time"
                :date="event.date"
                :title="event.title"
                variant="admin"
              />
          </template>
        </CardContent>
      </Card>

      <div class="w-full h-full rounded bg-blue-200"></div>
    </div>
  </div>
</template>

<style scoped>
</style>
