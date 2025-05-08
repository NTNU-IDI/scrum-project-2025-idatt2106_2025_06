<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

const verificationStatus = ref('');
const isLoading = ref(true);
const router = useRouter();
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
    const response = await axios.get(`http://localhost:8080/api/auth/email-verification/${token}`, {}, {
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
  } finally {
    isLoading.value = false;
  }
});

const goToLogin = () => {
  router.push('/login');
};
</script>

<template>
  <div class="flex flex-col items-center justify-center min-h-screen">
    <div v-if="isLoading" class="text-lg font-bold">Verifiserer...</div>
    <div v-else class="text-center">
      <p class="text-lg font-bold">{{ verificationStatus }}</p>
      <Button v-if="!isLoading" class="mt-4" @click="goToLogin">Gå til innlogging</Button>
    </div>
  </div>
</template>
