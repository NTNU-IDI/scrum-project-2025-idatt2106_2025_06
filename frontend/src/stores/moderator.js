// src/stores/moderator.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  createModerator,
  removeModerator,
  fetchModerators,
} from '@/service/moderatorService'

export const useModeratorStore = defineStore('moderator', () => {
  const moderators = ref([])
  const moderatorEmail = ref('')

  async function fetchAll(token){
    try {
      const response = await fetchModerators(token)
      console.error('Expected array', response)
      moderators.value = response.map(m => ({
        ...m,
        isCurrentUser: m.email === moderatorEmail.value
      }))
    } catch (error) {
      console.error('Error fetching moderators:', error)
    }
  }

  async function add(email, name, token) {
    try {
      await createModerator(email, name, token)
      await fetchAll(token)
    } catch (error) {
      console.error('Error adding moderator:', error)
    }
  }

  async function remove(email, id, name, token) {
    try {
      await removeModerator(email, name, id, token)
      await fetchAll(token)
    } catch (error) {
      console.error('Error removing moderator:', error)
    }
  }

  return { moderators, fetchAll,  add, remove }
})
