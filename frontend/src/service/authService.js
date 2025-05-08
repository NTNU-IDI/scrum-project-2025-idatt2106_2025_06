import api from '@/config/api.js'

export async function loginUser(email, password) {
  try {
    const response = await api.post('/auth/login', { email, password })
    return response.data;
  } catch (error) {
    console.error('Login error:', error);
    throw error;
  }
}

export async function signupUser(email, name, password) {
  try {
    const response = await api().post('/auth/signup', { email, name, password })
    return response.data;
  } catch (error) {
    console.error('Signup error:', error);
    throw error;
  }
}

export async function updateUser(name, email) {
  try {
    const response = await api.put('/user/edit/name-email', { name, email })
    return response.data
  } catch (error) {
    console.error('Update user error:', error)
    throw error
  }
}

export async function changePassword(oldPassword, newPassword) {
  try {
    const response = await api.put('/user/edit/password', {
      oldPassword: oldPassword,
      newPassword
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
    console.log(response.data)
    return response.data
  } catch (error) {
    console.error('Change password error:', error)
    throw error
  }
}

export async function changeLocationTracking(locationTracking) {
  try {
    const response = await api.put('/user/privacy-policy/change-location-tracking', { locationTracking })
    return response.data
  } catch (error) {
    console.error('Change location tracking error:', error)
    throw error
  }
}

export async function deleteLocationHistory() {
  try {
    const response = await api.post('/user/privacy-policy/delete-location-history')
    return response.data
  } catch (error) {
    console.error('Delete location history error:', error)
    throw error
  }
}
