<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import api from '@/config/api.js';

const verificationStatus = ref('');
const isLoading = ref(true);
const route = useRoute();

onMounted(async () => {
  const token = route.params.token;
  console.log(route.params.token);
  if (!token) {
    verificationStatus.value = 'Ugyldig eller manglende verifikasjonslenke.';
    isLoading.value = false;
    return;
  }

  try {
    const response = await api.get(`/auth/email-verification/${token}`, {}, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    if (response.status !== 200) {
      throw new Error('Feil ved bekreftelse av e-post.');
    }
    verificationStatus.value = 'E-posten din er bekreftet! Du kan nå logge inn.';
  } catch (error) {
    verificationStatus.value = 'Bekreftelse feilet. Lenken kan være utløpt eller ugyldig.';
    console.error("Error: ", error);
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div class="flex flex-col items-center justify-center min-h-screen">
    <div v-if="isLoading" class="text-lg font-bold">Verifiserer...</div>
    <div v-else class="text-center">
      <p class="text-lg font-bold">{{ verificationStatus }}</p>
      <RouterLink v-if="!isLoading" class="mt-4" to="/login">Gå til innlogging</RouterLink>
    </div>
  </div>
</template>
