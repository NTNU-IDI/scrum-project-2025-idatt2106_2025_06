<script setup>

import { BentoCard, BentoGrid } from '@/components/ui/bento/index.js'
import { ArrowRightIcon } from 'lucide-vue-next'
import { useScenarioStore } from '@/stores/scenario'
import { onMounted, computed } from 'vue'

const scenarioStore = useScenarioStore()
const displayedScenarios = computed(() => scenarioStore.scenarios)

onMounted(async () => {
  try {
    await scenarioStore.fetchScenarios()
    console.log(scenarioStore.scenarios)
  }
  catch (error) {
    console.log(error)
  }
})

</script>

<template>
  <div class="m-3 mt-5 xl:mx-auto">
  <BentoGrid>
    <BentoCard v-for="scenario in displayedScenarios" :key="scenario.id"
      :name="scenario.title"
      :description="scenario.description"
      :href="`/scenario/${scenario.id}`"
      cta="Les mer"
      :Icon="ArrowRightIcon"
      customClass="col-span-2 md:col-span-3 lg:col-span-2"
    />
  </BentoGrid>
  </div>
</template>

<style scoped>

</style>
