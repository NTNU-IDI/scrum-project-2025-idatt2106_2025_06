<script setup>
import { computed, watch } from 'vue'
import { RouterView } from 'vue-router'
import { useSessionStore } from '@/stores/session.js'
import Navbar from '@/components/NavBar.vue'
import NavBarAdmin from '@/components/NavBarAdmin.vue'
import Footer from '@/components/Footer.vue'

const session = useSessionStore()

const isModOrAdmin = computed(() => ['ROLE_ADMIN', 'ROLE_MODERATOR'].includes(session.user?.role))

watch(
  () => session.user,
  (newUser) => {
    if (newUser) {
      console.log('User session updated', newUser)
    }
  },
  { immediate: true },
)
</script>

<template>
  <main class="h-screen flex flex-col w-full">
    <Navbar v-if="!isModOrAdmin" />
    <NavBarAdmin v-else />
    <RouterView class="shrink-0 max-w-6xl min-h-[calc(100vh-3.5rem)]" />
    <Footer />
  </main>
</template>
