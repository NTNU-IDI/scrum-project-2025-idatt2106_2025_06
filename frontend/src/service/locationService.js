import { sendMessage } from '../lib/websocket.js';
import { useSessionStore } from '../stores/session.js';


let watchId = null;


export const startSendingUserLocation = (session) => {
  console.log('Starting user location...', session);
  const trackingEnabled = session.user?.trackingEnabled;
  console.log('Starting user location:', trackingEnabled);
  console.log(trackingEnabled)
  if (!trackingEnabled) {
    console.log('Tracking is disabled for this user.');
    return;
  }
  if (navigator.geolocation) {
    navigator.geolocation.watchPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        const locationData = {
          location: {
            latitude: parseFloat(latitude.toFixed(6)),
            longitude: parseFloat(longitude.toFixed(6)),
          },
        };
        sendMessage("/app/private/location/update", JSON.stringify(locationData), session.token);
      },
      (error) => {
        console.error("Error getting location:", error);
      },
      {
        enableHighAccuracy: true,
        maximumAge: 0,
        timeout: 5000,
      }
    );
  } else {
    console.error("Geolocation is not supported by this browser.");
  }}

export const stopSendingUserPosition = () => {
  if (watchId != null) {
    navigator.geolocation.clearWatch(watchId);
    watchId = null;
    console.log('Stopped sending user position.');
  }
};
