<script setup>
import AlertCard from '@/components/AlertCard.vue'
import EventCard from '@/components/EventCard.vue'
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { initializeWebSocket, sendMessage} from '@/lib/websocket.js'
import { useSessionStore} from '@/stores/session'
import { computed } from 'vue'


const alerts = ref([]);
const sessionStore = useSessionStore();
const jwtToken = sessionStore.token
console.log({ jwtToken });


const sendTestMessage = async () => {
    await new Promise(resolve => setTimeout(resolve, 2000));
  sendMessage('/app/public/newsAlerts', { message: 'Hello, world!' });
  console.log('5 seconds passed')
}

onMounted(() => {
  initializeWebSocket(jwtToken, (message) => {
    console.log('Received message:', message);
    const parsedMessages = typeof message === 'string' ? JSON.parse(message) : message;
    const messages = Array.isArray(parsedMessages) ? parsedMessages : [parsedMessages];

    messages.forEach((msg) => {
      const alert = {
        id: msg.id,
        title: msg.title || 'No Title',
        description: msg.description || 'No Description',
        severity: msg.severity || 'info',
        time: msg.time || 'Nå',
      };
      console.log('Parsed alert:', alert);
    alerts.value.push(alert);
    });
  });
  sendTestMessage()
});


const events = [
  {
    id: 1,
    title: 'GODE NYHETER! lalal',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
    time: 'Nå',
    severity: 'info',
  },
  {
    id: 2,
    title: 'Bolle',
    description:
      'Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang.Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang.Gratis bolle på Element. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang. Denne teksten er lang.',
    time: 'Nå',
    severity: 'red',
  },
  {
    id: 3,
    title: 'Gå vekk alerts',
    description: 'Snart skal alerts slutte å vises. Dette skal kunne scrolles plis',
    time: '11:42',
    severity: 'yellow',
  },
  {
    id: 4,
    title: 'Håp',
    description: 'Håper denne er borte.',
    time: '11:42',
    severity: 'green',
  },
  {
    id: 5,
    title: 'Bø',
    description: 'Borte... bø!.',
  },
  {
    id: 6,
    title: 'GODE NYHETER!',
    description: 'En bombe er sluppet på sluppen, alle eksamener avlyst.',
  },
  {
    id: 7,
    title: 'Bolle',
    description: 'Gratis bolle på Element.',
  },
]
</script>

<template>
  <div class="m-auto mt-2 h-full w-full">
    <div
      class="flex z-10 w-full gap-2 py-4 rounded-lg overflow-x-scroll scroll-smooth scrollbar-hide absolute max-w-6xl"
    >
      <template v-for="(alert, index) in alerts" :key="index">
        <RouterLink :to="'alerts/' + alert.id" class="relative">
          <AlertCard
            :description="alert.description"
            :severity="alert.severity"
            :time="alert.time"
            :title="alert.title"
            variant="expand"
          />
        </RouterLink>
      </template>
    </div>

    <div class="mt-16">
      <div class="flex flex-col gap-2">
        <div class="grid w-full gap-2 [grid-template-columns:repeat(auto-fit,minmax(20rem,1fr))]">
          <template v-for="(event, index) in events" :key="index">
            <RouterLink :to="'events/' + event.id" class="relative">
              <EventCard
                :description="event.description"
                :severity="event.severity"
                :time="event.time"
                :title="event.title"
                variant="default"
              />
            </RouterLink>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
