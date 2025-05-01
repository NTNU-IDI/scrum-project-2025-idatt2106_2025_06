<script setup>

import UpdateAlert from '@/components/UpdateAlert.vue'

const severityColors = {
  info: 'bg-blue-200',
  red: 'bg-red-500',
  yellow: 'bg-yellow-400',
  green: 'bg-green-400',
}

const props = defineProps({
  title: String,
  description: String,
  date: String,
  time: String,
  severity: { type: String, default: 'info' }, // "info", "red", "yellow", or "green"
  variant: { type: String, default: 'expand' }, // "expand", "short", or "admin"
})

const emit = defineEmits(['update'])

function handleUpdate(updatedAlert) {
  emit('update', updatedAlert) // send videre til forelderen (f.eks. AlertsList.vue)
}

</script>

<template>
  <div
    :class="[
      'flex shrink-0 overflow-hidden min-h-fit hover:bg-neutral-50 transition-all duration-100 gap-2 z-10 items-center rounded-lg border border-gray-200 bg-white px-4 py-2 shadow-sm',
      variant === 'expand' ? 'group cursor-pointer hover:gap-2 w-64' : 'w-full',
    ]"
  >
    <div class="flex flex-col justify-between items-start flex-grow min-w-0 relative">
      <div class="flex w-full justify-between items-center gap-2">
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
        :class="[
          'text-sm w-full text-gray-500 dark:text-gray-400 transition-all duration-100 overflow-hidden',
          variant === 'expand' ? 'max-h-0 group-hover:max-h-40' : 'truncate',
        ]"
      >
        {{ props.description }}
      </p>
    </div>
    <UpdateAlert
      v-if="props.variant === 'admin'"
      :title="props.title"
      :description="props.description"
      :type="props.severity"
      :date="props.date"
      :time="props.time"
      @update="handleUpdate"
    />
  </div>
</template>
