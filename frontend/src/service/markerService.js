import api from '@/config/api'

export async function getAllMarkers() {
  try {
    console.log('Fetching all markers..')
    const response = await api.get('/markers/getAll')
    console.log('Markers fetched successfully:', response.data)
    return response.data
  } catch (error) {
    console.error('Failed to fetch markers:', error)
    throw new Error('Could not load markers')
  }
}
