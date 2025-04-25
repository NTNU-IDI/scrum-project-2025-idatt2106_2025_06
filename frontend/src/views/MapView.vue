<script>
import {defineComponent} from "vue";
import Map from "@/components/Map.vue";
import {Card} from "@/components/ui/card/index.js";

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
  components: {Card, Map}
})

</script>

<template>
  <div class="m-auto flex relative flex-1 w-full py-10 gap-6">
    <Card class="w-72"></Card>
    <Map v-model="location"/>
  </div>
</template>

<style scoped></style>
