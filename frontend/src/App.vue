<script setup>
import { RouterView } from 'vue-router'
import Navbar from '@/components/NavBar.vue'
import { useSessionStore } from '@/stores/session.js'
import NavBarAdmin from '@/components/NavBarAdmin.vue'
import Footer from '@/components/footer.vue'
import { useWebSocketStore } from '@/stores/websocket.js'


const session = useSessionStore()
const admin = ['ROLE_ADMIN', 'ROLE_MODERATOR'].includes(session.user?.role)

const webSocketStore = useWebSocketStore()
webSocketStore.initialize(session.token)
</script>

<template>
  <main class="h-screen flex flex-col w-full">
    <Navbar v-if="!admin" />
    <NavBarAdmin v-else />
    <RouterView class="max-w-6xl min-h-[calc(100vh-3.5rem)]" />
    <Footer />
  </main>
</template>
