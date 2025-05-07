<script setup>
import ResetPasswordLink from '@/components/ResetPasswordLink.vue'
import { Button } from '@/components/ui/button/index.js'
import { computed } from 'vue'
import { useSessionStore } from '@/stores/session'
import { useRouter} from 'vue-router'

const router = useRouter()
const session = useSessionStore()

function handleLogout() {
  session.logout()
  router.push('/login')
}

const isAdmin = computed(() => session.user?.role === 'ADMIN')
</script>

<template>
  <nav class="sticky top-0 z-50 w-full py-2 shadow backdrop:white">
    <div class="flex items-center justify-between max-w-6xl m-auto">
      <router-link class="flex min-w-40 h-10 gap-2 items-center hover:scale-110 transition" to="/admin">
        <img alt="logo" height="32" src="/krisefikserLogo.svg" width="32">
        <p class="font-bold">KRISEFIKSER</p>
      </router-link>
      <div
        class="gap-10 hidden sm:flex items-center">
        <router-link to="/admin/addremove" v-if="isAdmin">
          <Button>Administrer moderatorer</Button>
        </router-link>
        <ResetPasswordLink v-if="isAdmin"/>
      </div>
        <Button @click="handleLogout" class="bg-white text-black hover:bg-gray-100" >Logg ut</Button>

    </div>

  </nav>
</template>
