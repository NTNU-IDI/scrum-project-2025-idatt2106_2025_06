<script setup>
import { useSessionStore } from '@/stores/session.js'
import { computed } from 'vue'

const sessionStore = useSessionStore();

const isLoggedIn = computed(() => sessionStore.token);
</script>

<template>
  <nav class="sticky top-0 z-50 w-full py-2 shadow">
    <div class="flex items-center justify-between max-w-6xl m-auto">
      <router-link class="flex min-w-40 h-10 gap-2 items-center hover:scale-110 transition" to="/">
        <img alt="logo" height="32" src="/krisefikserLogo.svg" width="32" />
        <p class="font-bold">KRISEFIKSER</p>
      </router-link>
      <div class="gap-10 hidden sm:flex h-10 items-center">
        <router-link class="hover:scale-110 transition" to="/alerts">Varslinger</router-link>
        <router-link class="hover:scale-110 transition" to="/inventory">Beredskapslager</router-link>
        <router-link class="hover:scale-110 transition" to="/map">Kart</router-link>
      </div>
      <router-link
        class="flex max-w-100 min-w-100 justify-end h-10 gap-4 items-center hover:scale-110 transition "
        to="/profile"
      >
        <p v-if="!isLoggedIn">Logg inn</p>
        <p v-else>Min profil</p>

      </router-link>
    </div>
  </nav>
</template>
