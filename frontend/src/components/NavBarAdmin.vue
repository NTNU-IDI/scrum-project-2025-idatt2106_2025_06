<script setup>
import DeleteModerator from '@/components/DeleteModerator.vue'
import ResetPasswordLink from '@/components/ResetPasswordLink.vue'
import AddModerator from '@/components/AddModerator.vue'
import { Button } from '@/components/ui/button/index.js'
import router from '@/router/router.js'
import { useSessionStore } from '@/stores/session.js'
const session = useSessionStore()
const isAdmin = ['ROLE_ADMIN'].includes(session.user?.role)

const handleLogout = async () => {
  try {
    session.logout()
    router.push('/login')
  } catch (error) {
    console.error('Logout failed:',error)
  }
}
</script>

<template>
  <nav class="sticky top-0 z-50 w-full py-2 shadow">
    <div class="flex items-center justify-between max-w-7xl m-auto">
      <router-link class="flex min-w-40 h-10 gap-4 items-center hover:scale-110 transition" to="/admin">
        <img alt="logo" src="">
        <p>Krisefikser</p>
      </router-link>
      <div
        class="gap-10 hidden sm:flex h-10 items-center">
        <AddModerator v-if="isAdmin"/>
        <DeleteModerator v-if="isAdmin"/>
        <ResetPasswordLink v-if="isAdmin"/>
      </div>
      <Button variant="destructive" @click="handleLogout">Logg ut</Button>
    </div>

  </nav>
</template>
