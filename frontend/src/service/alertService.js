import api from '@/config/api.js'

export async function fetchAlerts() {
  try {
    const response = await api.get('/alerts/getAll')
    return response.data
  } catch (error) {
    console.error('Login error:', error)
    throw error
  }
}

export async function createAlert(payload) {
  const res = await api.post('/alerts/create', {
    payload,
  })
  if (!res.ok) throw new Error('Kunne ikke opprette varsel')
  return await res.json()
}
