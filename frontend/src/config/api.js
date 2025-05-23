/**
 * API configuration settings
 */

import router from '@/router/router.js'
import { useSessionStore } from '@/stores/session'
import axios from 'axios'

export const API_CONFIG = {
  API_BASE_URL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  DEFAULT_PAGE_SIZE: 5,
  TIMEOUT: 10000, // 10 seconds
}

const api = axios.create({
  baseURL: API_CONFIG.API_BASE_URL,
  timeout: API_CONFIG.TIMEOUT,
})

api.interceptors.request.use(
  (config) => {
    const authStore = useSessionStore()
    if (authStore.token && authStore.token !== '' && !config.url?.includes('/auth')) {
      config.headers.Authorization = `Bearer ${authStore.token}`
    }

    return config
  },
  (error) => {
    return Promise.reject(new Error(error.message || 'An error occurred'))
  },
)

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      const authStore = useSessionStore()
      authStore.logout()
      router.push('/login')
    }
    return Promise.reject(new Error(error.message || 'An error occurred'))
  },
)

/**
 * Helper function to build API URLs with proper path handling
 */
export const buildApiUrl = (path, queryParams) => {
  const url = new URL(`${API_CONFIG.API_BASE_URL}/${path.replace(/^\//, '')}`)

  if (queryParams) {
    Object.entries(queryParams).forEach(([key, value]) => {
      url.searchParams.append(key, String(value))
    })
  }

  return url
}

export default api
