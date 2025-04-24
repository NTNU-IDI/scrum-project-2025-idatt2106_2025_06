<script>
import {defineComponent} from "vue";
import Map from "@/components/Map.vue";

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
  components: {Map}
})

</script>

<template>
  <div class="m-auto flex relative flex-1 w-full">
    <Map v-model="location"/>
  </div>
</template>

<style scoped></style>
