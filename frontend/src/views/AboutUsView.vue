<script setup>
import { onMounted, ref } from 'vue'

const aboutUsText = ref('')

onMounted(async () => {
  try {
    const response = await fetch('/omoss.txt')
    aboutUsText.value = await response.text()
  } catch (err) {
    console.error('Klarte ikke laste tekst:', err)
    aboutUsText.value = 'Kunne ikke laste tekst.'
  }
})

</script>

<template>
  <div class="flex flex-col items-center">
    <div class="p-6">
      <h1 class="text-2xl font-bold mb-4">Om oss</h1>
      <div
        class=" text-sm whitespace-pre-wrap">
        {{ aboutUsText }}
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
