// src/stores/scenario.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as scenarioService from '@/service/scenarioService'

export const useScenarioStore = defineStore('scenario', () => {
  const scenarios = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function fetchScenarios() {
    try {
      loading.value = true
      scenarios.value = await scenarioService.getAllScenarios()
    } catch (err) {
      error.value = err.message
    } finally {
      loading.value = false
    }
  }

  async function createScenario(scenarioData) {
    try {
      loading.value = true
      await scenarioService.createScenario(scenarioData)
      await fetchScenarios()
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  async function updateScenario(scenarioData) {
    try {
      loading.value = true
      await scenarioService.updateScenario(scenarioData)
      await fetchScenarios()
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  async function deleteScenario(scenarioId) {
    try {
      loading.value = true
      await scenarioService.deleteScenario(scenarioId)
      await fetchScenarios()
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    scenarios,
    loading,
    error,
    fetchScenarios,
    createScenario,
    updateScenario,
    deleteScenario
  }
})
