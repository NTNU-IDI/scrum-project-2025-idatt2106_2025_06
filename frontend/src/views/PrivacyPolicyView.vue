<script setup>
import { onMounted, ref } from 'vue'

const privacyText = ref('')

onMounted(async () => {
  try {
    const response = await fetch('/personvern.txt')
    privacyText.value = await response.text()
  } catch (err) {
    console.error('Klarte ikke laste personvernerklæring:', err)
    privacyText.value = 'Kunne ikke laste personvernerklæring.'
  }
})

</script>

<template>
  <div class="flex flex-col items-center">
    <div class="p-6">
      <h1 class="text-2xl font-bold mb-4">Personvernerklæring</h1>
      <div
        class=" text-sm whitespace-pre-wrap">
        {{ privacyText }}
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
