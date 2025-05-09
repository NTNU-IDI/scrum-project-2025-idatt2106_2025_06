<!-- components/ScenarioCard.vue -->
<script setup>
import { Button } from '@/components/ui/button'
import { PencilIcon, PlusIcon, TrashIcon } from 'lucide-vue-next'
import { ref, onMounted } from 'vue'
import ScenarioDialog from '@/components/ScenarioDialog.vue'
import { useScenarioStore } from '@/stores/scenario'
import { useSessionStore } from '@/stores/session'

const sessionStore = useSessionStore()
const scenarioStore = useScenarioStore()

const showDialog = ref(false)
const editingScenario = ref(null)

onMounted(async () => {
    await scenarioStore.fetchScenarios()
})

const handleCreate = () => {
  showDialog.value = true
  editingScenario.value = null
}

const handleEdit = (scenario) => {
  showDialog.value = true
  editingScenario.value = scenario
}

const handleClose = () => {
  showDialog.value = false
  editingScenario.value = null
}

const handleDelete = async (scenario) => {
  if (confirm('Er du sikker på at du vil slette scenariet?')) {
    try {
      await scenarioStore.deleteScenario(scenario.id, sessionStore.token)
      await scenarioStore.fetchScenarios(sessionStore.token)
    } catch (error) {
      alert('Error deleting scenario: ' + error.message)
    }
  }
}
</script>

<template>
  <div class="space-y-4">
    <div class="flex justify-end">
      <Button variant="outline" size="sm" @click="handleCreate">
        <PlusIcon class="h-4 w-4 mr-2" />
        Legg til nytt scenario
        </Button>
    </div>

    <div class="space-y-4">
      <div
        v-for="scenario in scenarioStore.scenarios"
        :key="scenario.id"
        class="flex items-center justify-between p-4 rounded-lg border"
      >
        <div>
          <h3 class="font-medium">{{ scenario.title }}</h3>
          <p class="text-sm text-muted-foreground">{{ scenario.description }}</p>
        </div>
        <div class="flex gap-2">
          <Button
            variant="ghost"
            size="icon"
            @click="() => handleEdit(scenario)"
          >
            <PencilIcon class="h-4 w-4" />
          </Button>
          <Button
            variant="gost"
            size="icon"
            @click="() => handleDelete(scenario)"
            class="text-red-500 hover:text-red-700 hover:bg-red-50"
          >
            <TrashIcon class="h-4 w-4" />
          </Button>
        </div>
      </div>
  </div>

  <ScenarioDialog
    v-if="showDialog"
    :scenario="editingScenario"
    @close="handleClose"
  />
  </div>
</template>
