import api from '@/config/api.js'
import axios from 'axios'

export async function createStorage(name, token, location) {
  try {
    const response = await api.post(
      '/storages/create',
      { name, location },
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );
    console.log("Response fra createStorage:", response.data)
    return response.data;
  } catch (error) {
    console.error('Create storage error:', error);
    throw error;
  }
}

export async function fetchStorages() {
  try {
    const response = await api.get('/storages/my-storages',
      {
        headers: {
          'Content-Type': 'application/json'
        }
      })
    return response.data
  } catch (error) {
    console.error('Fetch storages error:', error)
    throw error
  }
}

export async function fetchStorageMembers(storageId) {
  try {
    const response = await api.get(`/storages/${storageId}/members`)
    return response.data
  } catch (error) {
    console.error(`Failed to fetch members for storage ${storageId}:`, error)
    throw error
  }
}

export async function updateStorage(id, name, location) {
  try {
    const response = await api.put(`/storages/${id}`, {
      name,
      location,
    })
    return response.data
  } catch (error) {
    console.error(`Feil ved oppdatering av husstand ${id}:`, error)
    throw error
  }
}

export async function joinStorage(token, storageToken) {
  try {
    const response = await api.post('/storages/join',
      { token: storageToken })
    return response.data
  } catch (error) {
    console.error('Join storage error:', error)
    throw error
  }
}

export async function removeStorageMember(token, userId, storageId) {
  try {
    const response = await api.post(
      '/storages/remove-member',
      { userId, storageId })
    return response.data
  } catch (error) {
    console.error(`Feil ved fjerning av medlem ${userId} fra storage ${storageId}:`, error)
    throw error
  }
}

