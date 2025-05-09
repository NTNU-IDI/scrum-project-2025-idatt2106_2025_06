import api from '@/config/api'

export async function createMarker(createMarkerRequest) {
  try {
    console.log('Creating marker..')
    const response = await api.post('/markers/create', createMarkerRequest)
    return response.data
  } catch (error) {
    console.error('Failed to create marker:', error)
    const message = error.response?.data?.message || 'Kunne ikke opprette markør'
    throw new Error(message)
  }
}

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
    console.log('Startlocation: ', startLocation, 'Type: ', type)
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

export async function updateMarker(updateMarkerRequest) {
  try {
    console.log('Updating marker..')
    const response = await api.post('/markers/update', updateMarkerRequest)
    console.log('Marker updated successfully:', response.data)
    return response.data
  } catch (error) {
    console.error('Failed to update marker:', error)
    const message = error.response?.data?.message || 'Kunne ikke oppdatere markør'
    throw new Error(message)
  }
}

export async function deleteMarker(markerId) {
  try {
    console.log('Deleting marker..')
    const response = await api.delete(`/markers/delete/${markerId}`)
    console.log('Marker deleted successfully:')
    return response.data
  } catch (error) {
    console.error('Failed to delete marker:', error)
    throw new Error('Could not delete marker')
  }
}
