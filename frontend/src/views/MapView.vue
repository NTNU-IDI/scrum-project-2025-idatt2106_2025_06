<template>
  <div class="m-auto flex relative flex-1 w-full py-10 gap-6">
    <aside
      class="w-72 p-4 bg-white rounded-lg shadow space-y-4"
      style="position: relative; z-index: 10"
    >
      <div class="flex items-center justify-between">
        <div>
          <p class="text-sm font-semibold mb-1">Din posisjon</p>
          <p class="text-neutral-500">{{ $refs.mapRef?.statusMessage }}</p>
        </div>
        <Button variant="outline" @click="$refs.mapRef.flyToUser()">
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

      <div class="flex flex-col gap-6">
        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <MapPin class="size-5" />
              Dine markører
            </label>
            <Checkbox v-model="settings.showMarkers" />
          </div>
          <div v-if="settings.showMarkers" class="flex flex-col gap-2">
            <input
              v-model="settings.searchQuery"
              class="w-full border rounded px-2 py-1"
              placeholder="Søk..."
            />
            <Button>
              <MapPinPlus />
              Legg til ny markør
            </Button>
          </div>
        </div>
        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <Vault class="size-5" />
              Tilfluktsrom
            </label>
            <Checkbox v-model="settings.showShelters" />
          </div>
          <div v-if="settings.showShelters" class="flex flex-col gap-2">
            <input
              v-model="settings.searchQuery"
              class="w-full border rounded px-2 py-1"
              placeholder="Søk..."
            />
            <Button variant="outline">
              <Milestone />
              Vis rute til nærmeste tilfluktsrom
            </Button>
          </div>
        </div>
        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <HeartPulse class="size-5" />
              Hjertestartere
            </label>
            <Checkbox v-model="settings.showDefibrillators" />
          </div>
          <div v-if="settings.showDefibrillators" class="flex flex-col gap-2">
            <input
              v-model="settings.searchQuery"
              class="w-full border rounded px-2 py-1"
              placeholder="Søk..."
            />
            <Button variant="outline">
              <Milestone />
              Vis rute til nærmeste hjertestarter
            </Button>
          </div>
        </div>
        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <CookingPot class="size-5" />
              Matstasjoner
            </label>
            <Checkbox v-model="settings.showFoodStations" />
          </div>
          <div v-if="settings.showFoodStations" class="flex flex-col gap-2">
            <input
              v-model="settings.searchQuery"
              class="w-full border rounded px-2 py-1"
              placeholder="Søk..."
            />
            <Button variant="outline">
              <Milestone />
              Vis rute til nærmeste matstasjon
            </Button>
          </div>
        </div>
        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <MapPin class="size-5" />
              Lagre lokasjoner
            </label>
            <Checkbox v-model="settings.showStorages" />
          </div>
          <div v-if="settings.showStorages" class="flex flex-col gap-2">
            <input
              v-model="settings.searchQuery"
              class="w-full border rounded px-2 py-1"
              placeholder="Søk..."
            />
          </div>
        </div>
      </div>
    </aside>

    <Map
      ref="mapRef"
      v-model="location"
      :markers="markers"
      :settings="settings"
      :start-selection="startSelection"
      :storages="storages"
      class="flex-1"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { getAllMarkers } from '@/service/markerService'
import { fetchStorages } from '@/service/storageService'
import Map from '@/components/Map.vue'
import { Checkbox } from '@/components/ui/checkbox'
import { Button } from '@/components/ui/button'
import {
  CookingPot,
  HeartPulse,
  MapPin,
  MapPinPlus,
  Milestone,
  Navigation,
  Vault,
} from 'lucide-vue-next'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select/index.js'

const location = reactive({
  lng: 10.40574,
  lat: 63.41754,
  bearing: 0,
  pitch: 0,
  zoom: 12,
})

const settings = reactive({
  searchQuery: '',
  showMarkers: true,
  showShelters: true,
  showDefibrillators: true,
  showFoodStations: true,
  showStorages: true,
  minCapacity: 0,
  geoLocationEnabled: true,
})

const markers = ref([])
const storages = ref([])

const startSelection = ref('current')

const startOptions = computed(() => {
  const opts = [{ label: 'Din posisjon', value: 'current' }]
  for (const s of storages.value) {
    if (s.location?.longitude != null && s.location?.latitude != null) {
      opts.push({ label: s.name, value: s.id })
    }
  }
  return opts
})

onMounted(async () => {
  markers.value = await getAllMarkers()
  storages.value = await fetchStorages()
})
</script>

<style scoped>
aside {
  position: relative;
  z-index: 10;
}
</style>
