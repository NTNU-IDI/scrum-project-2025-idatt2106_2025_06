<script setup>
import { Info, OctagonAlert, Pencil, Shield, Trash, TriangleAlert } from 'lucide-vue-next'
import { Button } from '@/components/ui/button/index.js'
import { useEventStore } from '@/stores/event.js'
import { useSessionStore } from '@/stores/session'
import { defineEmits } from 'vue'

const eventStore = useEventStore()
const sessionStore = useSessionStore()

const emit = defineEmits(['edit'])

const severityColors = {
  info: 'bg-blue-200',
  high: 'bg-red-500',
  medium: 'bg-yellow-400',
  low: 'bg-green-400',
}

const props = defineProps({
  variant: { type: String, default: 'user' },

  // Obligatoriske props
  eventId: { type: [String, Number], required: true },
  name: { type: String, required: true },
  description: { type: String, required: true },
  type: { type: String, required: true },
  severity: { type: String, default: 'low' },
  status: { type: String, required: true },
  updatedAt: { type: [String, Object], required: true },

  // Ikke obligatoriske props
  content: { type: String, default: '' },
  startTime: { type: [String, Object], default: null },
  endTime: { type: [String, Object], default: null },
  location: { type: Object, default: null },
})

const handleDelete = async () => {
  try {
    await eventStore.deleteEventById(props.eventId, sessionStore.token)
  } catch (error) {
    console.error('Kunne ikke slette event:', error)
  }
}

const formattedDate = () => {
  if (!props.updatedAt) {
    console.warn(`updatedDate mangler for eventId: ${props.eventId}`)
    return 'Ukjent dato'
  }

  const dateObj = new Date(props.updatedAt)
  dateObj.setHours(dateObj.getHours() + 2) //Added because date somehow saves as CEST

  const today = new Date()

  const isSameDay =
    today.getFullYear() === dateObj.getFullYear() &&
    today.getMonth() === dateObj.getMonth() &&
    today.getDate() === dateObj.getDate()

  if (isSameDay) {
    return dateObj.toLocaleTimeString('no-NO', { hour: '2-digit', minute: '2-digit' })
  } else if (today.getFullYear() === dateObj.getFullYear()) {
    return (
      dateObj.toLocaleDateString('no-NO', { day: 'numeric', month: 'short' }) +
      ' ' +
      dateObj.toLocaleTimeString('no-NO', { hour: '2-digit', minute: '2-digit' })
    )
  } else {
    return dateObj.toLocaleDateString('no-NO', { day: 'numeric', month: 'short', year: 'numeric' })
  }
}

const handleEdit = () => {
  emit('edit', {
    eventId: props.eventId,
    name: props.name,
    description: props.description,
    content: props.content,
    severity: props.severity,
    type: props.type,
    status: props.status,
    location: props.location,
    startTime: props.startTime,
    endTime: props.endTime,
  })
}
</script>

<template>
  <div
    :class="[
      'relative flex shrink-0 h-full max-h-60 overflow-hidden gap-4 hover:bg-neutral-50 justify-between border rounded-lg p-4 transition-all duration-200 ease-in-out bg-white shadow-sm',
      variant === 'admin'
        ? 'max-h-20'
        : 'hover:shadow-md active:translate-y-1 hover:-translate-y-1',
    ]"
  >
    <div class="flex flex-col min-w-0 flex-grow">
      <div class="flex flex-col flex-grow min-h-0">
        <div class="flex justify-between items-start">
          <div class="flex items-center gap-2 min-w-0">
            <div
              :class="[
                'shrink-0 flex items-center justify-center rounded-full w-6 h-6',
                severityColors[props.severity] || severityColors['low'],
              ]"
            >
              <!-- Velg ikon basert på severity -->
              <Info v-if="props.severity === 'info'" class="w-4 h-4 text-white" />
              <OctagonAlert v-if="props.severity === 'high'" class="w-4 h-4 text-white" />
              <TriangleAlert v-if="props.severity === 'medium'" class="w-4 h-4 text-white" />
              <Shield v-if="props.severity === 'low'" class="w-4 h-4 text-white" />
            </div>

            <h1
              :class="[
                'font-bold',
                variant === 'admin' || variant === 'map' ? 'text-md' : 'text-2xl',
                variant === 'map' ? 'truncate overflow-hidden w-full min-w-0' : '',
              ]"
            >
              {{ props.name }}
            </h1>
          </div>

          <p
            v-if="variant !== 'map'"
            class="flex flex-end shrink-0 text-neutral-500 text-sm text-right"
          >
            {{ formattedDate() }}
          </p>
        </div>

        <p
          :class="[
            'text-md text-neutral-600 overflow-hidden',
            variant === 'admin' || variant === 'map' ? 'truncate' : '',
          ]"
        >
          {{ props.description || props.content }}
        </p>
      </div>
      <!-- TODO Legg til når les mer-funksjonalitet er på plass
      <div v-if="variant !== 'admin'" class="mt-4 flex-shrink-0">
        <p>Les mer</p>
      </div>-->
    </div>

    <div v-if="props.variant === 'admin'" class="flex items-center gap-2">
      <Button class="p-2" variant="outline" @click="handleEdit">
        <Pencil />
      </Button>
      <Button
        class="p-2 bg-red-600 text-white hover:bg-red-700 hover:text-white"
        variant="outline"
        @click="handleDelete"
      >
        <Trash />
      </Button>
    </div>
  </div>
</template>

<style scoped></style>
