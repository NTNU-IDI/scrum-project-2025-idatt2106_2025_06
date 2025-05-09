import api from '@/config/api'

export async function fetchEvents() {
  try {
    const response = await api.get('/events/all')
    return response.data
  } catch (error) {
    console.error('Feil ved henting av hendelser:', error)
    throw error
  }
}

export async function fetchEventById(eventId) {
  try {
    const response = await api.get(`/events/get/${eventId}`)
    return response.data
  } catch (error) {
    console.error(`Feil ved henting av hendelse med ID ${eventId}:`, error)
    throw error
  }
}

export async function createEvent(eventPayload) {
  try {
    const response = await api.post('/events/create', eventPayload)
    return response.data
  } catch (error) {
    console.error('Feil ved opprettelse av hendelse:', error)
    throw error
  }
}

export async function updateEvent(eventId, eventPayload) {
  try {
    const payloadWithId = { id: eventId, ...eventPayload }
    const response = await api.post('/events/update', payloadWithId)
    return response.data
  } catch (error) {
    console.error('Feil ved oppdatering av hendelse:', error)
    throw error
  }
}

export async function deleteEvent(eventId) {
  try {
    const response = await api.delete(`/events/delete/${eventId}`)
    return response.data
  } catch (error) {
    console.error('Feil ved sletting av hendelse:', error)
    throw error
  }
}
