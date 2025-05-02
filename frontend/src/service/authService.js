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
