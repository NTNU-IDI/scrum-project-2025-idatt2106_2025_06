<script>
import { defineComponent } from "vue";
import Map from "@/components/Map.vue";
import SearchAddressCoordinates from '@/components/SearchAddressCoordinates.vue';
import EditEvent from '@/components/EditEvent.vue';
import { Button } from '@/components/ui/button/index.js';
import { Input } from '@/components/ui/input/index.js'

export default defineComponent({
  data() {
    return {
      location: {
        lng: 10.40574,
        lat: 63.41754,
        bearing: 0,
        pitch: 0,
        zoom: 12
      },
      showSearchPopup: true
    };
  },
  methods: {
    toggleSearchPopup() {
      this.showSearchPopup = !this.showSearchPopup;
    }
  },
  components: {
    Input,
    Button,
    EditEvent,
    SearchAddressCoordinates,
    Map
  }
});
</script>


<template>
  <div class="m-auto flex flex-1 w-full py-10 gap-6">
    <!-- Venstre kolonne -->
    <div class="flex flex-col">
      <EditEvent />
    </div>

    <!-- Kart og popup -->
    <div class="flex relative flex-[2] h-[600px]">
      <SearchAddressCoordinates class="absolute top-4 left-4 z-20"
      />

      <!-- Kartet -->
      <div class="w-full h-full rounded bg-blue-200"></div>
    </div>
  </div>
</template>




<style scoped>
</style>
