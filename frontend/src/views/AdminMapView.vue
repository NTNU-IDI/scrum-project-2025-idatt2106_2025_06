<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useEventStore } from '@/stores/event.js'
import { useSessionStore } from '@/stores/session'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import EventForm from '@/components/EventForm.vue'
import MarkerForm from '@/components/MarkerForm.vue'
import Map from '@/components/Map.vue'
import { getAllMarkers } from '@/service/markerService'
import { fetchStorages } from '@/service/storageService'
import EventCard from '@/components/EventCard.vue'

const activeTab = ref('event')
const formMode = ref('new')
const selectedEvent = ref(null)
const selectedMarker = ref(null)

const eventStore = useEventStore()
const sessionStore = useSessionStore()
const events = computed(() => eventStore.events)

const location = reactive({ lng: 10.40574, lat: 63.41754, bearing: 0, pitch: 0, zoom: 12 })
const markers = ref([])
const storages = ref([])

const settings = reactive({
  searchQuery: '',
  showEvents: true,
  showPersonal: false,
  showGeneral: false,
  showShelters: false,
  showDefibrillators: false,
  showEmergencyClinics: false,
  showDistributionPoints: false,
  showPoliceStations: false,
  showPharmacies: false,
  showStorages: false,
  minCapacity: 0,
  geoLocationEnabled: true,
})

const startSelection = ref('current')

async function loadMarkers() {
  markers.value = await getAllMarkers()
}

onMounted(async () => {
  try {
    await eventStore.getEvents()
  } catch (e) {
    console.error('Kunne ikke hente hendelser:', e)
  }
  markers.value = await getAllMarkers()
  storages.value = await fetchStorages()
})

watch(activeTab, (tab) => {
  const isEventTab = tab === 'event'
  const isMarkerTab = tab === 'marker'

  settings.showEvents = isEventTab

  settings.showPersonal = isMarkerTab
  settings.showGeneral = isMarkerTab
  settings.showShelters = isMarkerTab
  settings.showDefibrillators = isMarkerTab
  settings.showEmergencyClinics = isMarkerTab
  settings.showDistributionPoints = isMarkerTab
  settings.showPoliceStations = isMarkerTab
  settings.showPharmacies = isMarkerTab
  settings.showStorages = isMarkerTab
})

function onNewEvent() {
  formMode.value = 'new'
  selectedEvent.value = null
}

function handleEditEvent(ev) {
  formMode.value = 'edit'
  selectedEvent.value = ev
}

function handleEditMarker(marker) {
  formMode.value = 'edit-marker'
  selectedMarker.value = marker
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
          <MarkerForm
            :marker-data="selectedMarker"
            :mode="formMode"
            @cancel="() => (formMode = 'new')"
            @saved="loadMarkers"
          />
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
            :impactAreaRadiusKm="ev.impactAreaRadiusKm"
            :location="ev.location"
            :mandatoryEvacuationAreaRadiusKm="ev.mandatoryEvacuationAreaRadiusKm"
            :name="ev.name"
            :recommendedEvacuationAreaRadiusKm="ev.recommendedEvacuationAreaRadiusKm"
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
        :events="activeTab === 'event' ? events : []"
        :markers="activeTab === 'marker' ? markers : []"
        :settings="settings"
        :start-selection="startSelection"
        :storages="activeTab === 'marker' ? storages : []"
        class="w-full h-full rounded"
        @select-event="handleEditEvent"
        @select-marker="handleEditMarker"
      />
    </div>
  </div>
</template>

<style scoped></style>
