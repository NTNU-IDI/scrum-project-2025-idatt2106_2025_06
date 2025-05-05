<script setup>
import { Pencil, Trash, Info, OctagonAlert, TriangleAlert, Shield } from 'lucide-vue-next'
import { Button } from '@/components/ui/button/index.js'

const severityColors = {
  info: 'bg-blue-200',
  red: 'bg-red-500',
  yellow: 'bg-yellow-400',
  green: 'bg-green-400',
}

const props = defineProps({
  title: String,
  description: String,
  time: String,
  date: String,
  position: String,
  severity: { type: String, default: 'info' },
  variant: String,
})

const isToday = () => {
  const today = new Date()
  const inputDate = new Date(props.date)
  return (
    today.getFullYear() === inputDate.getFullYear() &&
    today.getMonth() === inputDate.getMonth() &&
    today.getDate() === inputDate.getDate()
  )
}

const formatDate = () => {
  const dateObj = new Date(props.date)
  const today = new Date()

  // Skjul årstall hvis datoen er fra i år
  if (dateObj.getFullYear() === today.getFullYear()) {
    return dateObj.toLocaleDateString('no-NO', { month: 'short', day: 'numeric' })
  }
  // Vis årstall hvis datoen er fra et annet år
  return dateObj.toLocaleDateString('no-NO', { year: 'numeric', month: 'short', day: 'numeric' })
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
          <div class="flex items-center gap-2">
            <div
              :class="[
                'shrink-0 flex items-center justify-center rounded-full w-6 h-6',
                severityColors[props.severity] || severityColors['info'],
              ]"
            >
              <!-- Velg ikon basert på severity -->
              <Info v-if="props.severity === 'info'" class="w-4 h-4 text-white" />
              <OctagonAlert v-if="props.severity === 'red'" class="w-4 h-4 text-white" />
              <TriangleAlert v-if="props.severity === 'yellow'" class="w-4 h-4 text-white" />
              <Shield v-if="props.severity === 'green'" class="w-4 h-4 text-white" />
            </div>

            <h1 :class="['font-bold', variant === 'admin' ? 'text-md' : 'text-2xl']">
              {{ props.title }}
            </h1>
          </div>

          <p v-if="isToday()" class="text-neutral-500 text-sm">{{ props.time }}</p>
          <p v-if="!isToday()" class="text-neutral-500 text-sm">{{ formatDate() }}</p>
        </div>

        <p
          :class="[
            'text-md text-neutral-600 overflow-hidden',
            variant === 'admin' ? 'truncate' : '',
          ]"
        >
          {{ props.description }}
        </p>
        <p class="text-sm text-neutral-400 mt-1">{{ props.position }}</p>
      </div>
      <div v-if="variant !== 'admin'" class="mt-4 flex-shrink-0">
        <p>Les mer</p>
      </div>
    </div>

    <div v-if="props.variant === 'admin'" class="flex items-center gap-2">
      <Button class="p-2" variant="outline">
        <Pencil />
      </Button>
      <Button class="p-2 bg-red-600 text-white hover:bg-red-700 hover:text-white" variant="outline">
        <Trash />
      </Button>
    </div>
  </div>
</template>

<style scoped></style>
