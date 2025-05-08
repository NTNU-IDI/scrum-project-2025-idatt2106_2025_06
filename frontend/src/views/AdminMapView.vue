<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useEventStore } from '@/stores/event.js'
import { useSessionStore } from '@/stores/session'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import EventForm from '@/components/EventForm.vue'
import MarkerForm from '@/components/MarkerForm.vue'
import EventCard from '@/components/EventCard.vue'
import Map from '@/components/Map.vue'
import { getAllMarkers } from '@/service/markerService'
import { fetchStorages } from '@/service/storageService'

const activeTab = ref('event')
const formMode = ref('new')
const selectedEvent = ref(null)
const eventStore = useEventStore()
const sessionStore = useSessionStore()
const events = computed(() => eventStore.events)

const location = reactive({ lng: 10.40574, lat: 63.41754, bearing: 0, pitch: 0, zoom: 12 })
const markers = ref([])
const storages = ref([])
const settings = reactive({
  searchQuery: '',
  showPersonal: true,
  showGeneral: true,
  showShelters: true,
  showDefibrillators: true,
  showEmergencyClinics: true,
  showDistributionPoints: true,
  showPoliceStations: true,
  showPharmacies: true,
  showStorages: true,
  minCapacity: 0,
  geoLocationEnabled: true,
})
const startSelection = ref('current')

onMounted(async () => {
  try {
    await eventStore.getEvents()
  } catch (e) {
    console.error('Kunne ikke hente hendelser:', e)
  }
  markers.value = await getAllMarkers()
  storages.value = await fetchStorages()
})

function onNewEvent() {
  formMode.value = 'new'
  selectedEvent.value = null
}

function handleEditEvent(eventData) {
  formMode.value = 'edit'
  selectedEvent.value = eventData
}
</script>

<template>
  <div class="m-auto mt-10 flex flex-1 w-full py-10 gap-6">
    <RouterLink class="absolute z-10 top-20 left-30" to="/admin">
      <Button>Tilbake</Button>
    </RouterLink>

    <div class="flex flex-col">
      <Tabs v-model="activeTab" class="w-[400px]">
        <TabsList class="grid w-full grid-cols-2">
          <TabsTrigger value="event">Hendelse</TabsTrigger>
          <TabsTrigger value="marker">Markør</TabsTrigger>
        </TabsList>

        <TabsContent value="event">
          <EventForm
            :event-data="selectedEvent"
            :mode="formMode"
            :token="sessionStore.token"
            @edit="handleEditEvent"
          />
        </TabsContent>

        <TabsContent value="marker">
          <MarkerForm />
        </TabsContent>
      </Tabs>
    </div>

    <div class="flex relative flex-[2] h-[600px]">
      <div class="absolute top-4 left-4 z-20">
        <input
          v-model="settings.searchQuery"
          class="border rounded px-2 py-1"
          placeholder="Søk i kartet..."
          type="text"
        />
      </div>

      <Card v-if="activeTab === 'event'" class="h-[100%] absolute top-0 right-0 z-30">
        <CardHeader class="grid grid-cols-2 items-center w-full">
          <CardTitle class="text-2xl">Hendelser</CardTitle>
          <Button class="justify-self-end" @click="onNewEvent">Ny hendelse</Button>
        </CardHeader>
        <CardContent class="max-w-[350px] max-h-[85%] overflow-y-auto flex flex-col gap-2">
          <EventCard
            v-for="ev in events"
            :key="ev.id"
            :content="ev.content"
            :description="ev.description"
            :endTime="ev.endTime"
            :event-id="ev.id"
            :location="ev.location"
            :name="ev.name"
            :severity="ev.severity"
            :startTime="ev.startTime"
            :status="ev.status"
            :type="ev.type"
            :updatedAt="ev.updatedAt"
            variant="admin"
            @edit="handleEditEvent"
          />
        </CardContent>
      </Card>

      <Map
        ref="mapRef"
        v-model="location"
        :markers="markers"
        :settings="settings"
        :start-selection="startSelection"
        :storages="storages"
        class="w-full h-full rounded"
      />
    </div>
  </div>
</template>

<style scoped></style>
