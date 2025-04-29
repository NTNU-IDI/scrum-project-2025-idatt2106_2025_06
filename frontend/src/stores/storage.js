import { defineStore } from 'pinia'
import { createStorage } from '@/service/storageService.js'

export const useStorageStore = defineStore('session', () => {

  async function create(name) {
    try {
      const response = await createStorage(name)
      return response
    } catch (error) {
      console.error('Error creating storage:', error)
      return false
    }
  }

  async function editName(id, name) {
    try {
      const response = await editStorageName(id, name)
      return response
    } catch (error) {
      console.error('Error editing storage name:', error)
      return false
    }
  }

  async function editLocation(id, location) {
    try {
      const response = await editStorageLocation(id, location)
      return response
    } catch (error) {
      console.error('Error editing storage location:', error)
      return false
    }
  }

  return{ create }
})

