import axios from 'axios';

export async function fetchEvents() {
  try {
    const response = await axios.get('http://localhost:8080/api/events/all');
    return response.data;
  } catch (error) {
    console.error('Feil ved henting av hendelser:', error);
    throw error;
  }
}

export async function fetchEventById(eventId, token) {
  try {
    const response = await axios.get(`http://localhost:8080/api/events/${eventId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Feil ved henting av hendelse med ID ${eventId}:`, error);
    throw error;
  }
}

export async function createEvent(eventPayload, token) {
  try {
    const response = await axios.post('http://localhost:8080/api/events/create', eventPayload, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    return response.data;
  } catch (error) {
    console.error('Feil ved opprettelse av hendelse:', error);
    throw error;
  }
}

export async function updateEvent(eventId, eventPayload, token) {
  try {
    const response = await axios.put(`http://localhost:8080/api/events/update`, eventPayload, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    return response.data;
  } catch (error) {
    console.error('Feil ved oppdatering av hendelse:', error);
    throw error;
  }
}

export async function deleteEvent(eventId, token) {
  try {
    const response = await axios.delete(`http://localhost:8080/api/events/delete/${eventId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    return response.data;
  } catch (error) {
    console.error('Feil ved sletting av hendelse:', error);
    throw error;
  }
}
