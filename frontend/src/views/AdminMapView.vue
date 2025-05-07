<script setup>
import SearchAddressCoordinates from '@/components/SearchAddressCoordinates.vue';
import { Button } from '@/components/ui/button/index.js';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card/index.js';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs/index.js';
import { useEventStore } from '@/stores/event.js'
import { useSessionStore } from '@/stores/session'
import EventCard from '@/components/EventCard.vue';
import EventForm from '@/components/EventForm.vue'
import MarkerForm from '@/components/MarkerForm.vue'
import { computed } from 'vue';
import { onMounted } from 'vue'
import { ref } from 'vue';

const formMode = ref('new');

const eventStore = useEventStore()
const sessionStore = useSessionStore()

const events = computed(() => eventStore.events);

const selectedEvent = ref(null)

const handleEditEvent = (eventData) => {
  formMode.value = 'edit';
  selectedEvent.value = eventData
}

onMounted(async () => {
  try {
    await eventStore.getEvents()
  } catch (error) {
    console.error('Kunne ikke hente hendelser:', error)
  }
})

const eventFormRef = ref(null);

function onNewEvent() {
  formMode.value = 'new';
  eventFormRef.value?.eventFormRefresh();
}
const activeTab = ref('event');
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
          <EventForm
            ref="eventFormRef"
            :mode="formMode"
            :token="sessionStore.token"
            :event-data="selectedEvent"
          />
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
          <Button class="justify-self-end" @click="onNewEvent">Ny hendelse</Button>
        </CardHeader>
        <CardContent class="max-w-[350px] max-h-[85%] overflow-y-auto flex flex-col gap-2">
          <template v-for="event in events" :key="event.id">
            <EventCard
              variant="admin"
              :event-id="event.id"
              :name="event.name"
              :description="event.description"
              :content="event.content"
              :severity="event.severity"
              :type="event.type"
              :status="event.status"
              :location="event.location"
              :startTime="event.startTime"
              :endTime="event.endTime"
              :updatedAt="event.updatedAt"
              @edit="handleEditEvent"
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
