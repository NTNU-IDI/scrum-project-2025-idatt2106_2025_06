<script setup>
import { useRoute } from 'vue-router'
import { fetchEventById } from '@/service/eventService.js'
import { onMounted, ref } from 'vue'
import { Button } from '@/components/ui/button/index.js'

const route = useRoute()
const eventId = route.params.id
const isLoading = ref(true)

const event = ref(null)

onMounted(() => {
  console.log('Event ID:', eventId)
  getEvent(eventId)
})

async function getEvent(id) {
  try {
    event.value = await fetchEventById(id)
    console.log('Event: ', event.value.name)
    isLoading.value = false
  } catch (error) {
    console.error('Error fetching event:', error)
  }
}
</script>

<template>
  <div class="m-auto mt-4">
    <div v-if="!isLoading && event" class="my-auto">
      <div class="flex">
        <h1 class="font-bold text-4xl">{{ event.name }}</h1>
        <div class="flex-1"></div>
        <Button>Tilbake</Button>
      </div>
      <p>Publisert: {{ event.createdAt }}</p>
      <p>Sist oppdatert: {{ event.updatedAt }}</p>

      <p>{{ event.description }}</p>
      <p>{{ event.content }}</p>
    </div>
  </div>
</template>

