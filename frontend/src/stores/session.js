import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import router from '@/router'
import { loginUser, signupUser } from '@/service/authService.js'

export const useSessionStore = defineStore('session', () => {
  const token = ref(sessionStorage.getItem('token'))
  const user = ref(JSON.parse(sessionStorage.getItem('user') ?? 'null'))

  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => user.value && user.value.admin)

  function setToken(response) {
    if (!response) {
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('user')
      user.value = null
      token.value = null
      return
    }

    token.value = response.token
    sessionStorage.setItem('token', response.token)
    console.log('Token set:', token.value)
    console.log('User data:', response)
    user.value = {
      id: response.userId,
      email: response.email,
      name: response.name,
      joinedAt: response.joinedAt,
      admin: response.admin,
      permission: response.permission,
    }
    sessionStorage.setItem('user', JSON.stringify(user.value))
  }

  async function login(email, password) {
    try {
      const response = await loginUser(email, password)
      console.log('Permission:', response.permission)
      setToken(response)
      return true
    } catch (error) {
      console.error('Error logging in:', error)
      return false
    }
  }

  async function signup(email, name, password) {
    try {
      const response = await signupUser(email, name, password)
      setToken(response)
      return true
    } catch (error) {
      console.error('Error signing up:', error)
      return false
    }
  }

  function logout() {
    setToken(null)
    router.push('/login')
  }

  return { token, user, isAuthenticated, isAdmin, login, signup, logout }
})
