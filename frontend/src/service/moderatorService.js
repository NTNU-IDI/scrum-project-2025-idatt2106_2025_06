import api from '@/config/api.js'

export async function createModerator(name, email, username) {
  try {
    const response = await api.post('moderator/create', {
      name,
      email,
      username
    })
    return response.data;
  } catch (error) {
    console.error('Create moderator error:', error)
    throw error
  }
}

export async function fetchModerators() {
  try {
    const response = await api.get('/moderator/all')
    return response.data;
  } catch (error) {
    console.error('Fetch moderators error:', error)
    throw error
  }
}

export async function removeModerator(name, username, email, id) {
  try {
    const response = await api.post('/moderator/remove', {
      name, email, id
    })
    return response.data;
  } catch (error) {
    console.error('Remove admin error', error)
    throw error;
  }
}
