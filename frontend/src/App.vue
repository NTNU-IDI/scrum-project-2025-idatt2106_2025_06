<script setup>
import { RouterView } from 'vue-router'
import Navbar from '@/components/NavBar.vue'
import { useSessionStore } from '@/stores/session.js'
import NavBarAdmin from '@/components/NavBarAdmin.vue'

const session = useSessionStore()
const admin = ['ROLE_ADMIN', 'ROLE_MODERATOR'].includes(session.user?.role)
</script>

<template>
  <main class="h-screen flex flex-col w-full">
    <Navbar v-if="!admin" />
    <NavBarAdmin v-else />
    <div class="flex-1 container mx-auto px-4 py-8">
      <RouterView />
    </div>
  </main>
</template>
