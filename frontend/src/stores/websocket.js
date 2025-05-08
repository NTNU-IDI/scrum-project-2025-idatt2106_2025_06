import { defineStore } from 'pinia'
import { initializeWebSocket } from '@/lib/websocket.js'
import { reactive } from 'vue'

export const useWebSocketStore = defineStore('websocket', {
  state: () => reactive({
    alerts: [],
    events: [],
  }),
  actions: {
    addAlert(alert) {
      this.alerts.push(alert);
    },
    addEvent(event) {
      this.events.push(event);
    },
    initialize(jwtToken) {
      initializeWebSocket(jwtToken, (message, source) => {
        const parsed = typeof message === 'string' ? JSON.parse(message) : message;
        const messages = Array.isArray(parsed) ? parsed : [parsed];

        messages.forEach(message => {
          const formattedTime = message.startDate
            ? new Date(message.startDate).toLocaleString('en-US', { month: 'short', day: '2-digit', hour: '2-digit', hour12: false })
            : new Date().toLocaleString('NO', { month: 'short', day: '2-digit', hour: '2-digit', hour12: false });

          if (source === '/topic/public/newsAlerts') {
          this.addAlert({
            id: message.id,
            title: message.title || 'No Title',
            description: message.description || 'No Description',
            severity: message.severity || 'low',
            time: formattedTime || 'Now',
          });
        } else if (source === '/topic/public/events') {
            this.addEvent({
            id: message.id,
            title: message.name || 'No Title',
            description: message.description || 'No Description',
            content: message.content || 'No Content',
            severity: message.severity || 'low',
            time: formattedTime,
          });
        }})

      });
    },
  },
});
