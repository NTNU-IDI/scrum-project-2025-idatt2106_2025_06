<script setup>
import AlertCard from '@/components/AlertCard.vue'
import EventCard from '@/components/EventCard.vue'
import { RouterLink } from 'vue-router'

import { useWebSocketStore } from '@/stores/websocket.js';

const webSocketStore = useWebSocketStore();
const alerts = webSocketStore.alerts;
const events = webSocketStore.events;
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
          <template v-for="event in events" :key="event.id">
              <EventCard
                :event-id="event.id"
                :name="event.title"
                :description="event.description"
                :content="event.content"
                :severity="event.severity"
                :type="event.type"
                :status="event.status"
                :location="event.location"
                :updatedAt="event.time"
                variant="default"
              />
          </template>
        </div>
      </div>
    </div>
  </div>
</template>
