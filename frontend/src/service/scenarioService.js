// src/services/scenarioService.js
import api from '@/config/api.js'

export async function createScenario(scenario) {
  try {
    const response = await api.post(`scenarios/create`, scenario)
    return response.data;
  } catch (error) {
    console.error('Create scenario error:', error);
    throw error;
  }
}

export async function updateScenario(scenario) {
  try {
    const response = await api.post(`scenarios/update`, scenario)
    return response.data;
  } catch (error) {
    console.error('Update scenario error:', error);
    throw error;
  }
}

export async function getAllScenarios() {
  try {
    const response = await api.get(`scenarios/getAll`)
    return response.data;
  } catch (error) {
    console.error('Get scenarios error:', error);
    throw error;
  }
}

export async function deleteScenario(id) {
  try {
    const response = await api.delete(`scenarios/delete/${id}`)
    return response.data;
  } catch (error) {
    console.error('Delete scenario error:', error);
    throw error;
  }
}
