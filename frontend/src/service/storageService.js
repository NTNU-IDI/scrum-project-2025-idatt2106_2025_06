import axios from 'axios';

export async function createStorage(name) {
  try {
    const response = await axios.post('http://localhost:8080/api/storages/create', { name });
    return response.data;
  } catch (error) {
    console.error('Create storage error:', error);
    throw error;
  }
}
