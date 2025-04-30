<script setup>
import { Pencil } from 'lucide-vue-next'
import { Button } from '@/components/ui/button/index.js'

const props = defineProps({
  title: String,
  description: String,
  time: String,
  variant: String, // 'default' | 'admin'
})
</script>

<template>
  <div
    :class="[
      'flex h-full max-h-60 overflow-hidden gap-4 hover:bg-neutral-50 justify-between border rounded-lg p-4  transition-all duration-200 ease-in-out bg-white shadow-sm ',
      variant === 'admin'
        ? 'max-h-20'
        : 'hover:shadow-md active:translate-y-1 hover:-translate-y-1',
    ]"
  >
    <div class="flex flex-col min-w-0 flex-grow">
      <div class="flex flex-col flex-grow min-h-0">
        <div class="flex justify-between items-start">
          <h1 :class="['font-bold', variant === 'admin' ? 'text-md' : 'text-2xl']">
            {{ props.title }}
          </h1>
          <p class="text-neutral-500 text-sm">{{ props.time }}</p>
        </div>
        <p
          :class="[
            'text-md text-neutral-600 overflow-hidden',
            variant === 'admin' ? 'truncate' : '',
          ]"
        >
          {{ props.description }}
        </p>
      </div>
      <div v-if="variant !== 'admin'" class="mt-4 flex-shrink-0">
        <p>Les mer</p>
      </div>
    </div>

    <Button v-if="props.variant === 'admin'" class="p-2" variant="outline">
      <Pencil />
    </Button>
  </div>
</template>

<style scoped></style>
