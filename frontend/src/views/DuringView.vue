<script setup>
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
</script>

<template>
  <div class="m-auto mt-10">
    <Card class="mx-auto w-full items-center flex flex-col max-w-2xl p-6 shadow-md rounded-2xl">
      <CardHeader>
        <CardTitle class="text-xl font-bold">
          Under en krise
        </CardTitle>
      </CardHeader>
      <CardContent>
        <p class="text-base mt-2">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
          Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
          Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
          Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
        </p>
      </CardContent>
    </Card>

    <div class="mt-12 flex flex-wrap justify-center gap-6">
      <router-link to="/info">
        <Button class="px-10 py-6">
          Tilbake
        </Button>
      </router-link>
      <router-link to="/after">
        <Button class="px-10 py-6">
          Etter
        </Button>
      </router-link>
    </div>
  </div>
</template>


