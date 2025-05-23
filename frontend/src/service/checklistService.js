import api from '@/config/api.js'

export async function getAllCheckpoints() {
  try {
    const response = await api.get('/checkpoints',
    {
      headers: {
        'Content-Type': 'application/json'
      }
  })
    return response.data
  } catch (error) {
    console.error('Get all checkpoints error:', error)
    throw error
  }
}

export async function getAllMyCheckpoints() {
  try {
    const response = await api.get('/checkpoints/my-checkpoints',
      {
        headers: {
          'Content-Type': 'application/json'
        }
      })
    return response.data
  } catch (error) {
    console.error('Get all my checkpoints error:', error)
    throw error
  }
}

export async function updateCheckpoints(checkpoints) {
  try {
    const response = await api.post('/checkpoints/assign',
      {
        checkpointIds: checkpoints
      },
      {
        headers: {
          'Content-Type': 'application/json'
        }
      })
    return response.data
  } catch (error) {
    console.error('Get all checklist items error:', error)
    throw error
  }
}
