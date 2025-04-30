import { defineStore } from 'pinia'
import {
  createStorage, fetchStorageMembers, fetchStorages } from '@/service/storageService.js'
import { ref } from 'vue'

export const useStorageStore = defineStore('storage', () => {
  const storages = ref([])
  const membersByStorageId = ref({})

  async function fetchAll(token) {
    const response = await fetchStorages(token)

    if (!Array.isArray(response)) {
      console.error('Forventet at fetchStorages returnerer en liste, men fikk:', response)
      storages.value = []
      return
    }

    storages.value = response

    for (const storage of storages.value) {
      const members = await fetchStorageMembers(storage.id, token)
      membersByStorageId.value[storage.id] = members
    }
  }

  async function create(name, token) {
    const response = await createStorage(name, token)

    if (!Array.isArray(storages.value)) {
      console.warn('storages.value var ikke en liste – reinitialiserer den.')
      storages.value = []
    }

    storages.value.push(response)

    const members = await fetchStorageMembers(response.id, token)
    membersByStorageId.value[response.id] = members
    return response
  }

  /*
  async function join(token) {
    try {
      const response = await joinStorage(token)
      return response
    } catch (error) {
      console.error('Error joining storage:', error)
      return false
    }
  }

  async function editName(token, name) {
    try {
      const response = await editStorageName(token, name)
      return response
    } catch (error) {
      console.error('Error editing storage name:', error)
      return false
    }
  }

  async function editLocation(token, location) {
    try {
      const response = await editStorageLocation(token, location)
      return response
    } catch (error) {
      console.error('Error editing storage location:', error)
      return false
    }
  }

  async function removeMember(token, userId) {
    try {
      const response = await removeStorageMember(token, userId)
      return response
    } catch (error) {
      console.error('Error removing member:', error)
      return false
    }
  }

  async function getToken() {
    try {
      const response = await getStorageToken()
      return response
    } catch (error) {
      console.error('Error getting storage token:', error)
      return false
    }
  }

   */

  return{ fetchAll, create, storages, membersByStorageId }
})

