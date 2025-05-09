<script setup>
import ResetPasswordLink from '@/components/ResetPasswordLink.vue'
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
        <img alt="logo" height="32" src="/krisefikserLogo.svg" width="32" />
        <p class="font-bold">KRISEFIKSER</p>
      </router-link>
      <div
        class="gap-10 hidden sm:flex h-10 items-center">
        <router-link to="/admin/addremove">
          <Button variant="default" v-if="isAdmin">Administrer moderatorer</Button>
        </router-link>
        <ResetPasswordLink variant="default" v-if="isAdmin"/>
      </div>
      <router-link to="/profile">
        <Button @click="handleLogout">Logg ut</Button>
      </router-link>
    </div>

  </nav>
</template>
