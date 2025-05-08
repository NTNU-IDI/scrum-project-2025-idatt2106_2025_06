<script setup>

import { BentoCard } from '@/components/ui/bento/index.js'
import { useScenarioStore } from '@/stores/scenario'
import { useSessionStore } from '@/stores/session'
import { onMounted, computed } from 'vue'
import { ArrowRightIcon } from 'lucide-vue-next'

const scenarioStore = useScenarioStore()
const sessionStore = useSessionStore()
const displayedScenarios = computed(() => scenarioStore.scenarios)

onMounted(async () => {
  try {
    await scenarioStore.fetchScenarios(sessionStore.token || null)
    console.log(scenarioStore.scenarios)
  }
  catch (error) {
    console.log(error)
  }
})

</script>

<template>
  <div class="m-auto max-w-5xl p-6">
    <div class="grid grid-cols-2 gap-6">
      <div v-for="scenario in displayedScenarios" :key="scenario.id">
      <BentoCard
        :name="scenario.title"
        :description="scenario.description"
        :href="`/scenario/${scenario.id}`"
        cta="Les mer"
        :Icon="ArrowRightIcon"
      />
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
