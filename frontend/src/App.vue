<script setup>
import {RouterView} from 'vue-router'
import Navbar from "@/components/navbar.vue";
</script>

<template>
  <main class="h-screen flex flex-col w-full">
    <Navbar/>
    <RouterView class="max-w-4xl"/>
  </main>
</template>
