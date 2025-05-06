<script setup>
import AlertCard from '@/components/AlertCard.vue'
import EventCard from '@/components/EventCard.vue'
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { initializeWebSocket} from '@/lib/websocket.js'
import { useSessionStore} from '@/stores/session'


const alerts = ref([]);
const allNews = ref([]);
const sessionStore = useSessionStore();
const jwtToken = sessionStore.token
console.log({ jwtToken });


onMounted(() => {
  initializeWebSocket(jwtToken, (message, source) => {
    console.log('Received message from ',source, ': ', message);
    const parsedMessages = typeof message === 'string' ? JSON.parse(message) : message;
    const messages = Array.isArray(parsedMessages) ? parsedMessages : [parsedMessages];

    messages.forEach((msg) => {
      if (source === '/topic/public/newsAlerts') {
        const alert = {
          id: msg.id,
          title: msg.title || 'No Title',
          description: msg.description || 'No Description',
          severity: msg.severity || 'info',
          time: msg.time || 'Nå',
        };
        alerts.value.push(alert);
      }

      if (source === '/topic/public/events') {
        const news = {
          id: msg.id,
          title: msg.name || 'No Title',
          description: msg.description || 'No Description',
          content: msg.content || 'No Content',
          severity: msg.severity || 'info',
          time: msg.startDate || 'Nå',
        };
        allNews.value.push(news);
      }
    });
  });
  //sendTestMessage()
});
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
          <template v-for="(news, index) in allNews" :key="index">
            <RouterLink :to="'events/' + news.id" class="relative">
              <EventCard
                :description="news.description"
                :severity="news.severity"
                :time="news.startDate"
                :title="news.title"
                variant="default"
              />
            </RouterLink>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
