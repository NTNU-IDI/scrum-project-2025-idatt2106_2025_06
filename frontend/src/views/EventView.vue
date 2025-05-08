<script setup>
import { RadioIcon, CircleAlert} from 'lucide-vue-next'
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

const statusTranslations = {
  planned: 'Planlagt',
  ongoing: 'Pågår',
  finished: 'Ferdig',
  cancelled: 'Avbrutt',
}

const severityTranslations = {
  high: 'Høy',
  medium: 'Medium',
  low: 'Lavt',
}

function translate(translation, status) {
  return translation[status] || status
}



function formatDate(dateStr) {
  const date = new Date(dateStr)
  return date.toLocaleDateString('nb-NO', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

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
  <div class="m-auto mt-4 p-10 max-w-3xl">
    <div v-if="!isLoading && event" class="my-auto space-y-6">
      <div class="flex items-center mb-6">
        <h1 class="font-bold text-4xl">{{ event.name }}</h1>
        <div class="flex-1"></div>
        <RouterLink to="/alerts">
          <Button>Tilbake</Button>
        </RouterLink>
      </div>

      <p class="text-lg">{{ event.description }}</p>

      <div>
      <div class="flex items-center gap-2">
        <RadioIcon class="w-5 h-5 text-blue-400" />
        <p>Status: {{ translate(statusTranslations, event.status) }}</p>
      </div>
      <div class="flex items-center gap-2">
        <CircleAlert class="w-5 h-5 text-blue-400" />
        <p>Beredskapsnivå: {{ translate(severityTranslations, event.severity) }}</p>
      </div>
      </div>

      <p>{{ event.content }}</p>

      <div class="text-gray-500 space-y-1">
        <p>Publisert: {{ formatDate(event.createdAt) }}</p>
        <p>Sist oppdatert: {{ formatDate(event.updatedAt) }}</p>
      </div>
    </div>
  </div>
</template>

