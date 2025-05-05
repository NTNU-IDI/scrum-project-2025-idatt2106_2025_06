import {defineStore} from 'pinia'
import {
  createEvent,
  deleteEvent,
  fetchEventById,
  fetchEvents,
  updateEvent,
} from '@/service/eventService.js'
import {ref} from 'vue'

export const useEventStore = defineStore('eventStore', () => {

  const events = ref([])

  const getEvents = async () => {
    try {
      events.value = await fetchEvents()
    } catch (error) {
      console.error('Feil ved henting av hendelser:', error)
    }
  }

  const getEventById = async (eventId, token) => {
    try {
      return await fetchEventById(eventId, token)
    } catch (error) {
      console.error(`Feil ved henting av hendelse med ID ${eventId}:`, error)
      throw error
    }
  }


  const createNewEvent = async (eventPayload, token) => {
    try {
      const createdEvent = await createEvent(eventPayload, token)
      events.value.push(createdEvent)
    } catch (error) {
      console.error('Feil ved opprettelse av hendelse:', error)
    }
  }

  const updateExistingEvent = async (eventId, eventPayload, token) => {
    try {
      const updatedEvent = await updateEvent(eventId, eventPayload, token)
      const index = events.value.findIndex(event => event.id === eventId)
      if (index !== -1) {
        events.value[index] = updatedEvent
      }
    } catch (error) {
      console.error('Feil ved oppdatering av hendelse:', error)
    }
  }

  const deleteEventById = async (eventId, token) => {
    try {
      await deleteEvent(eventId, token)
      events.value = events.value.filter(event => event.id !== eventId)
    } catch (error) {
      console.error('Feil ved sletting av hendelse:', error)
    }
  }

  return {
    events,
    getEvents,
    getEventById,
    createNewEvent,
    updateExistingEvent,
    deleteEventById
  }
})
