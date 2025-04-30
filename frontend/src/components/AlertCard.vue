<script setup>
import { Button } from '@/components/ui/button/index.js'
import { Pencil } from 'lucide-vue-next'

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
  severity: { type: String, default: 'info' }, // "info", "red", "yellow", or "green"
  variant: { type: String, default: 'expand' }, // "expand", "title-only", or "admin"
})
</script>

<template>
  <div
    :class="[
      'flex w-72 overflow-hidden transition-all duration-100 gap-2 z-10 items-center rounded-lg border border-gray-200 bg-white p-4 shadow-sm',
      variant === 'expand' ? 'group cursor-pointer hover:gap-2' : '',
    ]"
  >
    <div class="flex flex-col justify-between items-start flex-grow min-w-0 relative">
      <div class="flex w-full justify-between items-center">
        <div class="flex items-center gap-2 flex-grow min-w-0">
          <div
            :class="[
              'icon flex min-h-full absolute top-[-1rem] left-[-1rem] min-w-2 w-2 h-96 items-center justify-center rounded-bl-lg rounded-tl-lg',
              severityColors[props.severity] || severityColors['info'],
            ]"
          />
          <h5 class="font-medium text-gray-900 overflow-hidden truncate">
            {{ props.title }}
          </h5>
        </div>
        <p class="text-sm text-gray-500 flex-shrink-0">
          {{ props.time }}
        </p>
      </div>
      <p
        v-if="variant !== 'title-only'"
        :class="[
          'text-sm w-full text-gray-500 dark:text-gray-400 transition-all duration-100 overflow-hidden',
          variant === 'expand' ? 'max-h-0 group-hover:max-h-40' : 'truncate',
        ]"
      >
        {{ props.description }}
      </p>
    </div>
    <Button v-if="props.variant === 'admin'" class="p-2" variant="outline">
      <Pencil />
    </Button>
  </div>
</template>
