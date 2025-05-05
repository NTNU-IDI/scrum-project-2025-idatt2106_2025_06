import axios from 'axios';



export async function createModerator(name, email, token) {
  try {
    const response = await axios.post('http://localhost:8080/api/moderator/create',
      { name, email },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      }
    )
    return response.data;
  } catch (error) {
    console.error('Create moderator error:', error)
    throw error
  }
}

export async function fetchModerators(token) {
  try {
    const response = await axios.get('http://localhost:8080/api/moderator/all',
      {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      }
    )
    return response.data;
  } catch (error) {
    console.error('Fetch moderators error:', error)
    throw error
  }

}


export async function removeModerator(email, token) {
  try {
    const response = await axios.post('http://localhost:8080/api/moderator/remove',
      { email },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    )
    return response.data;
  } catch (error) {
    console.error('Remove admin error', error)
    throw error;
  }
}
