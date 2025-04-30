<script setup>
const props = defineProps({
  title: String,
  description: String,
  time: String,
})
</script>

<template>
  <div
    class="flex h-full flex-col max-h-60 overflow-hidden hover:bg-neutral-100 justify-between border rounded-lg p-4 hover:-translate-y-1 transition-all duration-200 ease-in-out bg-white shadow-sm hover:shadow-md active:translate-y-1"
  >
    <div class="flex flex-col flex-grow min-h-0">
      <div class="flex justify-between items-start">
        <h1 class="text-2xl font-bold">{{ props.title }}</h1>
        <p class="text-neutral-500 text-sm">{{ props.time }}</p>
      </div>

      <p class="text-md text-neutral-600 overflow-hidden">{{ props.description }}</p>
    </div>
    <div class="mt-4 flex-shrink-0">
      <p>Les mer</p>
    </div>
  </div>
</template>

<style scoped></style>
