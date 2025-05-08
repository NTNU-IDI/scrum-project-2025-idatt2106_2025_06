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

export async function getClosestMarkerId(startLocation, type) {
  try {
    console.log('Fetching closest marker..')
    const response = await api.post('/markers/closest', {
      startLocation,
      type,
    })
    console.log('Closest marker ID fetched successfully:', response.data.markerId)
    return response.data.markerId
  } catch (error) {
    console.error('Failed to fetch closest marker:', error)
    throw new Error('Could not load closest marker')
  }
}
