<template>
  <div class="m-auto flex relative flex-1 w-full h-full py-10 gap-6 items-stretch min-h-0">
    <aside
      class="w-72 h-full p-4 bg-white rounded-lg shadow space-y-4 overflow-auto"
      style="position: relative; z-index: 10"
    >
      <div class="flex items-center justify-between">
        <div>
          <p class="text-sm font-semibold mb-1">Din posisjon</p>
          <p class="text-neutral-500">{{ mapRef?.statusMessage }}</p>
        </div>
        <Button class="size-10" variant="outline" @click="mapRef.flyToUser()">
          <Navigation />
        </Button>
      </div>

      <div class="mb-6">
        <label class="block text-sm font-semibold mb-1">Start for rute</label>
        <Select v-model="startSelection" class="w-full">
          <SelectTrigger>
            <SelectValue placeholder="Velg sted" />
          </SelectTrigger>
          <SelectContent>
            <SelectGroup>
              <SelectItem v-for="opt in startOptions" :key="opt.value" :value="opt.value">
                {{ opt.label }}
              </SelectItem>
            </SelectGroup>
          </SelectContent>
        </Select>
      </div>

      <div class="flex flex-col">
        <Tabs v-model="activeTab" class="w-full">
          <TabsList class="grid w-full grid-cols-2">
            <TabsTrigger value="event">Hendelser</TabsTrigger>
            <TabsTrigger value="marker">Markører</TabsTrigger>
          </TabsList>

          <TabsContent value="event">
            <div class="flex flex-col gap-2">
              <div class="flex items-center justify-between">
                <label class="items-center text-sm font-semibold">
                  Vis hendelser ({{ events.length }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :variant="settings.showEvents ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showEvents = !settings.showEvents"
                  >
                    <Eye v-if="settings.showEvents" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>
              <div v-if="settings.showEvents" class="flex flex-col gap-2">
                <RouterLink v-for="event in events" :key="event.id" :to="'alerts/' + event.id">
                  <EventCard
                    :key="event.id"
                    :content="event.content"
                    :description="event.description"
                    :event-id="event.id"
                    :name="event.name"
                    :severity="event.severity"
                    :status="event.status"
                    :type="event.type"
                    :updated-at="event.updatedAt"
                    variant="map"
                  />
                </RouterLink>
              </div>
            </div>
          </TabsContent>

          <TabsContent value="marker">
            <div class="mb-6">
              <input
                v-model="settings.searchQuery"
                class="w-full border rounded px-2 py-1"
                placeholder="Søk etter markør..."
                type="text"
              />
            </div>
            <div class="flex items-center gap-2 mb-6 text-neutral-500">
              <span>Trykk på </span>
              <div class="border size-6 flex justify-center items-center rounded-md">
                <Navigation class="size-3 text-neutral-500" />
              </div>
              <span> for å finne nærmeste</span>
            </div>
            <div class="flex flex-col gap-2">
              <div class="flex justify-between items-center mb-2">
                <label
                  :class="{
                    'flex items-center gap-2 text-sm font-semibold': true,
                    'text-neutral-400': !settings.showStorages,
                  }"
                >
                  <Warehouse class="size-5" />
                  Dine lagre ({{ counts.storages }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :variant="settings.showStorages ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showStorages = !settings.showStorages"
                  >
                    <Eye v-if="settings.showStorages" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>

              <div class="flex justify-between items-center mb-2">
                <label
                  :class="{
                    'flex items-center gap-2 text-sm font-semibold': true,
                    'text-neutral-400': !settings.showPersonal,
                  }"
                >
                  <MapPin class="size-5" />
                  Dine markører ({{ counts.personal }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :variant="settings.showPersonal ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showPersonal = !settings.showPersonal"
                  >
                    <Eye v-if="settings.showPersonal" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>

              <div class="flex justify-between items-center mb-2">
                <label
                  :class="{
                    'flex items-center gap-2 text-sm font-semibold': true,
                    'text-neutral-400': !settings.showShelters,
                  }"
                >
                  <Vault class="size-5" />
                  Tilfluktsrom ({{ counts.shelter }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :disabled="!settings.showShelters"
                    size="icon"
                    variant="outline"
                    @click="mapRef.flyToNearest('Shelter')"
                  >
                    <Navigation class="size-5" />
                  </Button>
                  <Button
                    :variant="settings.showShelters ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showShelters = !settings.showShelters"
                  >
                    <Eye v-if="settings.showShelters" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>

              <div class="flex justify-between items-center mb-2">
                <label
                  :class="{
                    'flex items-center gap-2 text-sm font-semibold': true,
                    'text-neutral-400': !settings.showDefibrillators,
                  }"
                >
                  <HeartPulse class="size-5" />
                  Hjertestartere ({{ counts.defibrillator }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :disabled="!settings.showDefibrillators"
                    size="icon"
                    variant="outline"
                    @click="mapRef.flyToNearest('Defibrillator')"
                  >
                    <Navigation class="size-5" />
                  </Button>
                  <Button
                    :variant="settings.showDefibrillators ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showDefibrillators = !settings.showDefibrillators"
                  >
                    <Eye v-if="settings.showDefibrillators" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>

              <div class="flex justify-between items-center mb-2">
                <label
                  :class="{
                    'flex items-center gap-2 text-sm font-semibold': true,
                    'text-neutral-400': !settings.showEmergencyClinics,
                  }"
                >
                  <Hospital class="size-5" />
                  Akuttmottak ({{ counts.emergencyClinic }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :disabled="!settings.showEmergencyClinics"
                    size="icon"
                    variant="outline"
                    @click="mapRef.flyToNearest('EmergencyClinic')"
                  >
                    <Navigation class="size-5" />
                  </Button>
                  <Button
                    :variant="settings.showEmergencyClinics ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showEmergencyClinics = !settings.showEmergencyClinics"
                  >
                    <Eye v-if="settings.showEmergencyClinics" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>

              <div class="flex justify-between items-center mb-2">
                <label
                  :class="{
                    'flex items-center gap-2 text-sm font-semibold': true,
                    'text-neutral-400': !settings.showDistributionPoints,
                  }"
                >
                  <Package class="size-5" />
                  Distribusjonspunkt ({{ counts.distributionPoint }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :disabled="!settings.showDistributionPoints"
                    size="icon"
                    variant="outline"
                    @click="mapRef.flyToNearest('DistributionPoint')"
                  >
                    <Navigation class="size-5" />
                  </Button>
                  <Button
                    :variant="settings.showDistributionPoints ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showDistributionPoints = !settings.showDistributionPoints"
                  >
                    <Eye v-if="settings.showDistributionPoints" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>

              <div class="flex justify-between items-center mb-2">
                <label
                  :class="{
                    'flex items-center gap-2 text-sm font-semibold': true,
                    'text-neutral-400': !settings.showPoliceStations,
                  }"
                >
                  <Shield class="size-5" />
                  Politistasjoner ({{ counts.policeStation }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :disabled="!settings.showPoliceStations"
                    size="icon"
                    variant="outline"
                    @click="mapRef.flyToNearest('PoliceStation')"
                  >
                    <Navigation class="size-5" />
                  </Button>
                  <Button
                    :variant="settings.showPoliceStations ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showPoliceStations = !settings.showPoliceStations"
                  >
                    <Eye v-if="settings.showPoliceStations" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>

              <div class="flex justify-between items-center mb-2">
                <label
                  :class="{
                    'flex items-center gap-2 text-sm font-semibold': true,
                    'text-neutral-400': !settings.showPharmacies,
                  }"
                >
                  <Plus class="size-5" />
                  Apotek ({{ counts.pharmacy }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :disabled="!settings.showPharmacies"
                    size="icon"
                    variant="outline"
                    @click="mapRef.flyToNearest('Pharmacy')"
                  >
                    <Navigation class="size-5" />
                  </Button>
                  <Button
                    :variant="settings.showPharmacies ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showPharmacies = !settings.showPharmacies"
                  >
                    <Eye v-if="settings.showPharmacies" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>

              <div class="flex justify-between items-center mb-2">
                <label
                  :class="{
                    'flex items-center gap-2 text-sm font-semibold': true,
                    'text-neutral-400': !settings.showGeneral,
                  }"
                >
                  <MapPin class="size-5" />
                  Andre markører ({{ counts.general }})
                </label>
                <div class="flex items-center gap-2">
                  <Button
                    :disabled="!settings.showGeneral"
                    size="icon"
                    variant="outline"
                    @click="mapRef.flyToNearest('General')"
                  >
                    <Navigation class="size-5" />
                  </Button>
                  <Button
                    :variant="settings.showGeneral ? 'default' : 'outline'"
                    size="icon"
                    @click="settings.showGeneral = !settings.showGeneral"
                  >
                    <Eye v-if="settings.showGeneral" class="size-5" />
                    <EyeOff v-else class="size-5 text-neutral-500" />
                  </Button>
                </div>
              </div>
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </aside>

    <Map
      ref="mapRef"
      v-model="location"
      :events="events"
      :markers="markers"
      :settings="settings"
      :start-selection="startSelection"
      :storages="storages"
      class="flex-1 h-full min-h-0"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { getAllMarkers } from '@/service/markerService'
import Map from '@/components/Map.vue'
import { Button } from '@/components/ui/button'
import {
  Eye,
  EyeOff,
  HeartPulse,
  Hospital,
  MapPin,
  Navigation,
  Package,
  Plus,
  Shield,
  Vault,
  Warehouse,
} from 'lucide-vue-next'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import EventCard from '@/components/EventCard.vue'
import { fetchEvents } from '@/service/eventService.js'
import { useSessionStore } from '@/stores/session.js'
import { fetchStorages } from '@/service/storageService.js'

const activeTab = ref('event')
const mapRef = ref(null)
const location = reactive({ lng: 10.40574, lat: 63.41754, bearing: 0, pitch: 0, zoom: 12 })
const settings = reactive({
  showPersonal: true,
  showGeneral: true,
  showShelters: true,
  showDefibrillators: true,
  showEmergencyClinics: true,
  showDistributionPoints: true,
  showPoliceStations: true,
  showPharmacies: true,
  showStorages: true,
  showEvents: true,
  searchQuery: '',
  minCapacity: 0,
})
const markers = ref([])
const storages = ref([])
const events = ref([])
const startSelection = ref('current')

const sessionStore = useSessionStore()
const user = computed(() => sessionStore.user)

const startOptions = computed(() => {
  const opts = [{ label: 'Din posisjon', value: 'current' }]
  for (const s of storages.value) {
    if (s.location?.longitude != null && s.location?.latitude != null) {
      opts.push({ label: s.name, value: s.id })
    }
  }
  return opts
})

const counts = computed(() => ({
  personal: markers.value.filter((m) => m.type === 'Personal').length,
  general: markers.value.filter((m) => m.type === 'General').length,
  shelter: markers.value.filter((m) => m.type === 'Shelter').length,
  defibrillator: markers.value.filter((m) => m.type === 'Defibrillator').length,
  emergencyClinic: markers.value.filter((m) => m.type === 'EmergencyClinic').length,
  distributionPoint: markers.value.filter((m) => m.type === 'DistributionPoint').length,
  policeStation: markers.value.filter((m) => m.type === 'PoliceStation').length,
  pharmacy: markers.value.filter((m) => m.type === 'Pharmacy').length,
  storages: storages.value.length,
}))

onMounted(async () => {
  markers.value = await getAllMarkers()
  if (user.value) {
    storages.value = await fetchStorages()
  }
  events.value = await fetchEvents()
})
</script>

<style scoped>
aside {
  position: relative;
  z-index: 10;
}
</style>
