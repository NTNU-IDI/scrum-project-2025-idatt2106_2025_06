import axios from 'axios';

export async function createStorage(name, token){
  try {
    const response = await axios.post('http://localhost:8080/api/storages/create',
      { name },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      }
    )
    return response.data;
  } catch (error) {
    console.error('Create storage error:', error);
    throw error;
  }
}

export async function fetchStorages(token) {
  try {
    const response = await axios.get('http://localhost:8080/api/storages/my-storages', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    return response.data
  } catch (error) {
    console.error('Fetch storages error:', error)
    throw error
  }
}

export async function fetchStorageMembers(storageId, token) {
  try {
    const response = await axios.get(`http://localhost:8080/api/storages/${storageId}/members`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    return response.data
  } catch (error) {
    console.error(`Failed to fetch members for storage ${storageId}:`, error)
    throw error
  }
}

export async function updateStorage(id, name, location, token) {
  try {
    const response = await axios.put(`http://localhost:8080/api/storages/${id}`, {
      name,
      location,
    }, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    })
    return response.data
  } catch (error) {
    console.error(`Feil ved oppdatering av husstand ${id}:`, error)
    throw error
  }
}

export async function joinStorage(token, storageToken) {
  try {
    const response = await axios.post('http://localhost:8080/api/storages/join',
      { token: storageToken },
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    )
    return response.data
  } catch (error) {
    console.error('Join storage error:', error)
    throw error
  }
}

export async function removeStorageMember(token, userId, storageId) {
  try {
    const response = await axios.post(
      'http://localhost:8080/api/storages/remove-member',
      { userId, storageId },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    )
    return response.data
  } catch (error) {
    console.error(`Feil ved fjerning av medlem ${userId} fra storage ${storageId}:`, error)
    throw error
  }
}

