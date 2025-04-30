import { defineStore } from 'pinia'
import { createStorage, fetchStorageMembers, fetchStorages, updateStorage, joinStorage, removeStorageMember } from '@/service/storageService.js'
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

  async function editStorage(id, name, location, token) {
    const updated = await updateStorage(id, name, location, token)

    // Oppdater lokalt lagret storage
    const index = storages.value.findIndex(s => s.id === id)
    if (index !== -1) {
      storages.value[index].name = updated.name
      storages.value[index].location = updated.location
    }

    return updated
  }

  async function join(storageToken, token) {
      await joinStorage(token, storageToken)
      await fetchAll(token)
      return true
  }

  async function removeMember(userId, storageId, token) {
      await removeStorageMember(token, userId, storageId)
      await fetchAll(token)
      return true
  }

  return{ fetchAll, create, join, storages, membersByStorageId, editStorage, removeMember }
})

