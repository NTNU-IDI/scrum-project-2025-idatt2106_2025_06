<template>
  <div class="m-auto flex relative flex-1 w-full h-full py-10 gap-6 items-stretch min-h-0">
    <aside
      class="w-72 h-full p-4 bg-white rounded-lg shadow space-y-4 overflow-auto"
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
              Generelle markører
            </label>
            <Checkbox v-model="settings.showGeneral" />
          </div>
          <div v-if="settings.showGeneral" class="flex flex-col gap-2">
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
              Vis nærmeste tilfluktsrom
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
              Vis nærmeste hjertestarter
            </Button>
          </div>
        </div>

        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <Hospital class="size-5" />
              Akuttmottak
            </label>
            <Checkbox v-model="settings.showEmergencyClinics" />
          </div>
          <div v-if="settings.showEmergencyClinics" class="flex flex-col gap-2">
            <input
              v-model="settings.searchQuery"
              class="w-full border rounded px-2 py-1"
              placeholder="Søk..."
            />
            <Button variant="outline">
              <Milestone />
              Vis nærmeste akuttmottak
            </Button>
          </div>
        </div>

        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <Package class="size-5" />
              Distribusjonspunkt
            </label>
            <Checkbox v-model="settings.showDistributionPoints" />
          </div>
          <div v-if="settings.showDistributionPoints" class="flex flex-col gap-2">
            <input
              v-model="settings.searchQuery"
              class="w-full border rounded px-2 py-1"
              placeholder="Søk..."
            />
            <Button variant="outline">
              <Milestone />
              Vis nærmeste distribusjonspunkt
            </Button>
          </div>
        </div>

        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <Shield class="size-5" />
              Politistasjoner
            </label>
            <Checkbox v-model="settings.showPoliceStations" />
          </div>
          <div v-if="settings.showPoliceStations" class="flex flex-col gap-2">
            <input
              v-model="settings.searchQuery"
              class="w-full border rounded px-2 py-1"
              placeholder="Søk..."
            />
            <Button variant="outline">
              <Milestone />
              Vis nærmeste politistasjon
            </Button>
          </div>
        </div>

        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <Plus class="size-5" />
              Apotek
            </label>
            <Checkbox v-model="settings.showPharmacies" />
          </div>
          <div v-if="settings.showPharmacies" class="flex flex-col gap-2">
            <input
              v-model="settings.searchQuery"
              class="w-full border rounded px-2 py-1"
              placeholder="Søk..."
            />
            <Button variant="outline">
              <Milestone />
              Vis nærmeste apotek
            </Button>
          </div>
        </div>

        <div>
          <div class="flex justify-between items-center mb-2">
            <label class="flex items-center gap-2 text-sm font-semibold">
              <Warehouse class="size-5" />
              Dine lagre
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
  HeartPulse,
  Hospital,
  MapPin,
  MapPinPlus,
  Milestone,
  Navigation,
  Package,
  Plus,
  Shield,
  Vault,
  Warehouse,
} from 'lucide-vue-next'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'

const location = reactive({
  lng: 10.40574,
  lat: 63.41754,
  bearing: 0,
  pitch: 0,
  zoom: 12,
})

const settings = reactive({
  showGeneral: true,
  showShelters: true,
  showDefibrillators: true,
  showEmergencyClinics: true,
  showDistributionPoints: true,
  showPoliceStations: true,
  showPharmacies: true,
  showStorages: true,
  searchQuery: '',
  minCapacity: 0,
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
