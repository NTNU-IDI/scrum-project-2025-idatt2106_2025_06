<script setup>
const event = {
  title: 'Event Title',
  description: 'This is a description of the event.',
  date: '2023-10-01',
}
</script>

<template>
  <div
    class="flex items-center justify-between rounded-lg border border-gray-200 bg-white p-4 shadow-sm dark:border-gray-700 dark:bg-gray-800"
  >
    <div class="flex flex-col gap-2">
      <div class="flex flex-col gap-1">
        <h5 class="mb-1 font-medium leading-none tracking-tight text-gray-900 dark:text-white">
          {{ event.title }}
        </h5>
        <p class="text-sm text-gray-500 dark:text-gray-400">
          {{ event.description }}
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
