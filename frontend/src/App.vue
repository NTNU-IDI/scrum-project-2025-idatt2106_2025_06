<script setup>
import {RouterView} from 'vue-router'
import Navbar from "@/components/NavBar.vue";
</script>

<template>
  <main class="h-screen flex flex-col w-full">
    <Navbar/>
    <RouterView class="max-w-6xl"/>
  </main>
</template>
