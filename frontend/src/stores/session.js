import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import router from '@/router/router.js'
import { changePassword, loginUser, signupUser, updateUser } from '@/service/authService.js'

export const useSessionStore = defineStore('session', () => {
  const token = ref(sessionStorage.getItem('token'))
  const user = ref(JSON.parse(sessionStorage.getItem('user') ?? 'null'))

  const isModerator = computed(() => user.value?.role === 'MODERATOR')
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  const isAuthenticated = computed(() => !!token.value)
  const hasAccessToAdmin = computed(() =>
    ['ADMIN', 'MODERATOR'].includes(user.value?.role)
  )

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
    user.value = {
      id: response.id,
      email: response.email,
      name: response.name,
      joinedAt: response.joinedAt,
      admin: response.admin,
      role: response.role,
      permissions: response.permissions
    }
    sessionStorage.setItem('user', JSON.stringify(user.value))
  }

  async function login(email, password) {
    try {
      const response = await loginUser(email, password)
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

  async function updateProfile(name, email) {
    try {
      const response = await updateUser(name, email)
      setToken(response)
      return true
    } catch (error) {
      console.error('Error updating profile:', error)
      return false
    }
  }

  async function updatePassword(oldPassword, newPassword, confirmPassword) {
    if (newPassword !== confirmPassword) {
      console.error('New password and confirmation do not match')
      return false
    } else {
      try {
        await changePassword(oldPassword, newPassword)
        return true
      } catch (error) {
        console.error('Error changing password:', error)
        return false
      }
    }
  }

  function logout() {
    setToken(null)
    router.push('/login')
  }

  return { token, user, isModerator, isAdmin, hasAccessToAdmin, isAuthenticated, login, signup, logout, updateProfile, updatePassword }
})
