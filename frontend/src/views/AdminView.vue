<script setup>
import { Button } from '@/components/ui/button/index.js'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import PublishAlert from '@/components/PublishAlert.vue'
import { onMounted, reactive, ref } from 'vue'
import Map from '@/components/Map.vue'
import { getAllMarkers } from '@/service/markerService.js'
import { fetchStorages } from '@/service/storageService.js'
import ScenarioCard from '@/components/ScenarioCard.vue'



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


onMounted(async () => {
  markers.value = await getAllMarkers()
  storages.value = await fetchStorages()
})
</script>

<template>
  <div class="m-auto flex flex-wrap relative w-full py-10 gap-6 items-stretch mt-10">
    <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
      <Card class="max-h-[650px] flex-1 basis-1/4 min-w-[300px]">
        <CardHeader>
          <CardTitle>Rediger scenario informasjon</CardTitle>
        </CardHeader>
        <CardContent class="flex flex-col overflow-y-auto max-h-[500px] gap-2">
          <ScenarioCard />
        </CardContent>
      </Card>

      <Card class="max-h-[650px] flex-1 basis-1/4 min-w-[300px]">
        <CardHeader class="grid grid-cols-2 items-center w-full">
          <CardTitle class="text-2xl">Varslinger</CardTitle>
          <PublishAlert />
        </CardHeader>
        <CardContent class="flex flex-col overflow-y-auto max-h-[500px] gap-2">
          Kommer snart:)
        </CardContent>
      </Card>

      <Card class="max-h-[650px] flex-1 basis-1/4 min-w-[300px]">
        <CardHeader class="grid grid-cols-2 items-center w-full">
          <CardTitle class="text-2xl whitespace-nowrap">Kart og hendelser</CardTitle>
          <router-link class="justify-self-end" to="/admin/map">
            <Button>Rediger</Button>
          </router-link>
        </CardHeader>
        <CardContent class="h-[85%]">
          <Map
            v-model="location"
            :markers="markers"
            :settings="settings"
            :storages="storages"
            class="w-full h-full rounded"
            start-selection="current"
          />
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<style scoped></style>
