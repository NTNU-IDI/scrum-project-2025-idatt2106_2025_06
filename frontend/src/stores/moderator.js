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

  async function fetchAll(){
    try {
      const response = await fetchModerators()
      moderators.value = response.map(m => ({
        ...m,
        isCurrentUser: m.email === moderatorEmail.value
      }))
    } catch (error) {
      console.error('Error fetching moderators:', error)
    }
  }

  async function add(name, email, username) {
    try {
      await createModerator(name, email, username)
      await fetchAll()
    } catch (error) {
      console.error('Error adding moderator:', error)
    }
  }

  async function remove(name, email, id, username) {
    try {
      await removeModerator(name, email, id, username)
      await fetchAll()
    } catch (error) {
      console.error('Error removing moderator:', error)
    }
  }

  return { moderators, fetchAll,  add, remove }
})
