// mock/moderatorApiMock.js
let mockModerators = [
  { id: 1, name: 'Ola Normann', email: 'admin1@example.com' },
  { id: 2, name: 'Kari Haugen', email: 'admin2@example.com' }
]

export async function fetchModerators() {
  return new Promise(resolve => setTimeout(() => resolve(mockModerators), 500))
}

export async function createModerator(name, email) {
  return new Promise(resolve => {
    setTimeout(() => {
      const newMod = { id: Date.now(), name, email }
      mockModerators.push(newMod)
      resolve({ ...newMod, message: 'Admin opprettet' })
    }, 500)
  })
}

export async function removeModerator(email) {
  return new Promise(resolve => {
    setTimeout(() => {
      mockModerators = mockModerators.filter(mod => mod.email !== email)
      resolve({ message: 'Admin fjernet' })
    }, 500)
  })
}

export async function getCurrentUser() {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve({ email: 'meg@example.com', isAdmin: true })
    }, 300)
  })
}
