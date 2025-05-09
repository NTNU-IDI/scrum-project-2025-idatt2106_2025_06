<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useScenarioStore } from '@/stores/scenario'
import { useSessionStore } from '@/stores/session'

const route = useRoute()
const scenarioStore = useScenarioStore()
const sessionStore = useSessionStore()
const scenario = ref(null)

const formatDate = (isoDate) => {
  const date = new Date(isoDate)
  return date.toLocaleDateString('no-NO', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

onMounted(async () => {
  if (!scenarioStore.scenarios.length) {
    await scenarioStore.fetchScenarios(sessionStore.token)
  }
  scenario.value = scenarioStore.scenarios.find(s => s.id === route.params.id)
})

</script>

<template>
    <div class="max-w-4xl m-10 p-6">
      <div v-if="scenario">
        <h1 class="text-3xl font-bold mb-4">{{ scenario.title }}</h1>
        <p>Opprettet: {{ formatDate(scenario.updatedAt) }}</p>
        <p>Sist endret: {{ scenario.createdAt }} </p>
        <p class="text-lg mb-2 text-gray-700">{{ scenario.description }}</p>
        <div class="prose mb-4">
          {{ scenario.content }}
        </div>
      </div>
      <div v-else class="text-center text-gray-500">
        Laster scenario...
      </div>
    </div>
</template>
