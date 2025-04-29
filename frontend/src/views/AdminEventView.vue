<script>
import {defineComponent} from "vue";
import Map from "@/components/Map.vue";
import SearchAddressCoordinates from '@/components/SearchAddressCoordinates.vue'
import EditEvent from '@/components/EditEvent.vue'


export default defineComponent({
  data: () => ({
    location: {
      lng:  10.40574,
      lat:  63.41754,
      bearing: 0,
      pitch: 0,
      zoom: 12
    }
  }),
  components: {
    EditEvent,
    SearchAddressCoordinates,
    Map
  }
})
</script>

<template>
  <div class="m-auto flex relative flex-1 w-full py-10 gap-6">
    <div class="flex flex-col">
      <SearchAddressCoordinates></SearchAddressCoordinates>
      <EditEvent></EditEvent>
    </div>
    <Map v-model="location" class="flex-[2]" />
  </div>
</template>

<style scoped>
</style>
