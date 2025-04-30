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
    return response.data // En liste av storages
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
    return response.data // Liste av medlemmer
  } catch (error) {
    console.error(`Failed to fetch members for storage ${storageId}:`, error)
    throw error
  }
}


/*
export async function joinStorage(token) {
  try {
    const response = await axios.post('http://localhost:8080/api/storages/join', { token });
    return response.data;
  } catch (error) {
    console.error('Join storage error:', error);
    throw error;
  }
}

export async function editStorageName(token, name) {
  try {
    const response = await axios.post('http://localhost:8080/api/storages/', { token, name });
    return response.data;
  } catch (error) {
    console.error('Edit storage name error:', error);
    throw error;
  }
}

export async function editStorageLocation(token, location) {
  try {
    const response = await axios.post('http://localhost:8080/api/storages/', { token, location });
    return response.data;
  } catch (error) {
    console.error('Edit storage location error:', error);
    throw error;
  }
}

export async function removeStorageMember(token, userId) {
  try {
    const response = await axios.post('http://localhost:8080/api/storages/remove-member', { token, userId });
    return response.data;
  } catch (error) {
    console.error('Remove storage member error:', error);
    throw error;
  }
}

export async function getStorageToken() {
  try {
    const response = await axios.get('http://localhost:8080/api/storages/token');
    return response.data;
  } catch (error) {
    console.error('Get storage token error:', error);
    throw error;
  }
}
  */
