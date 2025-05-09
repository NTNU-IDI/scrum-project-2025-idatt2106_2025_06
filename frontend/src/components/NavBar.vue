<script setup>
import { useSessionStore } from '@/stores/session.js'
import { computed, ref } from 'vue'
import { Menu } from 'lucide-vue-next';

const sessionStore = useSessionStore();

const isLoggedIn = computed(() => sessionStore.token);

const hamburgerMenu = ref(false);
</script>

<template>
  <nav class="sticky top-0 z-50 w-full py-2 shadow backdrop-blur-lg bg-white/75">
    <div class="flex items-center justify-between max-w-6xl m-auto pl-3">
      <router-link class="flex min-w-40 h-10 gap-2 items-center hover:scale-110 transition" to="/">
        <img alt="logo" height="32" src="/krisefikserLogo.svg" width="32" />
        <p class="font-bold">KRISEFIKSER</p>
      </router-link>
      <div class="gap-10 hidden sm:flex h-10 items-center">
        <router-link class="hover:scale-110 transition" to="/alerts">Varslinger</router-link>
        <router-link class="hover:scale-110 transition" to="/inventory">Beredskapslager</router-link>
        <router-link class="hover:scale-110 transition" to="/map">Kart</router-link>
      </div>
      <div class="flex min-w-40 justify-end h-10 pr-3 items-center" >
        <router-link class="hover:scale-110 transition hidden sm:block" to="/profile">
          <p v-if="!isLoggedIn">Logg inn</p>
          <p v-else>Min profil</p>
        </router-link>
        <button class="block sm:hidden" @click="hamburgerMenu = !hamburgerMenu">
          <Menu/>
        </button>
      </div>
    </div>
  </nav>
  <div v-if="hamburgerMenu" class="text-3xl fixed top-[64px] left-0 right-0 bottom-0 z-40 bg-white/90 flex flex-col items-end p-6 md:hidden">
    <router-link class="py-2 hover:scale-105 transition" to="/alerts" @click="hamburgerMenu = false">Varslinger</router-link>
    <router-link class="py-2 hover:scale-105 transition" to="/inventory" @click="hamburgerMenu = false">Beredskapslager</router-link>
    <router-link class="py-2 hover:scale-105 transition" to="/map" @click="hamburgerMenu = false">Kart</router-link>
    <router-link class="py-2 hover:scale-105 transition" to="/profile" @click="hamburgerMenu = false">
      <p v-if="!isLoggedIn">Logg inn</p>
      <p v-else>Min profil</p>
    </router-link>
  </div>
</template>
