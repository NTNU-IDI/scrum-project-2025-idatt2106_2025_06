<template>
  <div class="m-auto flex relative flex-1 w-full py-10 gap-6">
    <aside
      class="w-72 p-4 bg-white rounded-lg shadow space-y-4"
      style="position: relative; z-index: 10"
    >
      <div class="flex flex-col gap-10">
        <div class="flex items-center justify-between">
          <div class="flex w-full flex-col">
            <p class="block text-sm font-semibold mb-1">Din posisjon</p>
            <p class="text-neutral-500">Aktivert</p>
          </div>
          <Button class="size-5 h-auto" variant="outline" @click="$refs.mapRef.flyToUser()">
            <Navigation />
          </Button>
        </div>
        <div class="flex flex-col gap-6">
          <div>
            <div class="flex w-full justify-between">
              <label class="text-sm font-semibold mb-2 flex gap-2 items-center" for="showShelters">
                <MapPin class="size-5" />
                Dine markører</label
              >
              <Checkbox v-model="settings.showMarkers" />
            </div>
            <div class="flex flex-col gap-2">
              <input
                v-if="settings.showMarkers"
                v-model="settings.searchQuery"
                class="w-full border rounded px-2 py-1"
                placeholder="Søk..."
                type="text"
              />
              <Button v-if="settings.showMarkers" class="w-full">
                <MapPinPlus />
                Legg til ny markør
              </Button>
            </div>
          </div>
          <div>
            <div class="flex w-full justify-between">
              <label class="text-sm font-semibold mb-2 flex gap-2 items-center" for="showShelters">
                <Vault class="size-5" />
                Tilfluktsrom</label
              >
              <Checkbox v-model="settings.showShelters" />
            </div>
            <div class="flex flex-col gap-2">
              <input
                v-if="settings.showShelters"
                v-model="settings.searchQuery"
                class="w-full border rounded px-2 py-1"
                placeholder="Søk..."
                type="text"
              />
              <Button v-if="settings.showShelters" class="w-full" variant="outline">
                <Milestone />
                Vis rute til nærmeste tilfluktsrom
              </Button>
            </div>
          </div>
          <div>
            <div class="flex w-full justify-between">
              <label class="text-sm font-semibold mb-2 flex gap-2 items-center" for="showShelters">
                <HeartPulse class="size-5" />
                Hjertestartere
              </label>
              <Checkbox v-model="settings.showDefibrillators" />
            </div>
            <div class="flex flex-col gap-2">
              <input
                v-if="settings.showDefibrillators"
                v-model="settings.searchQuery"
                class="w-full border rounded px-2 py-1"
                placeholder="Søk..."
                type="text"
              />
              <Button v-if="settings.showDefibrillators" class="w-full" variant="outline">
                <Milestone />
                Vis rute til nærmeste hjertestarter
              </Button>
            </div>
          </div>
          <div>
            <div class="flex w-full justify-between">
              <label class="text-sm font-semibold mb-2 flex gap-2 items-center" for="showShelters">
                <CookingPot class="size-5" />
                Matstasjoner
              </label>
              <Checkbox v-model="settings.showFoodStations" />
            </div>
            <div class="flex flex-col gap-2">
              <input
                v-if="settings.showFoodStations"
                v-model="settings.searchQuery"
                class="w-full border rounded px-2 py-1"
                placeholder="Søk..."
                type="text"
              />
              <Button v-if="settings.showFoodStations" class="w-full" variant="outline">
                <Milestone />
                Vis rute til nærmeste matstasjon
              </Button>
            </div>
          </div>
        </div>
      </div>
    </aside>

    <Map ref="mapRef" v-model="location" :settings="settings" class="flex-1" />
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import {
  CookingPot,
  HeartPulse,
  MapPin,
  MapPinPlus,
  Milestone,
  Navigation,
  Vault,
} from 'lucide-vue-next'

import Map from '@/components/Map.vue'
import { Checkbox } from '@/components/ui/checkbox/index.js'
import { Button } from '@/components/ui/button/index.js'

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
  minCapacity: 0,
  geoLocationEnabled: true,
})
</script>

<style scoped>
aside {
  position: relative;
  z-index: 10;
}
</style>
