<script setup>
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useSessionStore } from '@/stores/session'

const email = ref('')
const password = ref('')

const errorMessage = ref('')

const router = useRouter()

const session = useSessionStore()

async function login() {
  errorMessage.value = ''
  try {
    const success = await session.login(email.value, password.value)
    if (success) {
      if (['ROLE_ADMIN', 'ROLE_MODERATOR'].includes(session.user?.role)) {
        await router.push('/admin')
      } else {
        await router.push('/')
      }
    } else {
      errorMessage.value = 'Innlogging feilet. Kontroller e-post, passord og at du har bekreftet eposten din.'
    }
  } catch (error) {
    if (error.response && error.response.status === 403) {
      errorMessage.value = 'Feil e-post eller passord.'
    } else if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message
    } else {
      errorMessage.value = 'Innlogging feilet. Prøv igjen senere.'
    }
    console.log('Error: ', errorMessage.value)
    return null
  }
}
</script>

<template>
  <div class="m-auto">
    <Card class="mx-auto max-w-sm">
      <CardHeader>
        <CardTitle class="text-2xl"> Logg inn</CardTitle>

        <CardDescription> Skriv inn epost-adresse og passord for å logge inn</CardDescription>
      </CardHeader>
      <CardContent>
        <div class="grid gap-4">
          <form @submit.prevent="login">
            <div class="grid gap-2">
              <Label for="email">Epost</Label>
              <Input id="email" v-model="email" required type="email" />

              <div class="flex items-center">
                <Label for="password">Passord</Label>
                <!--
                <a class="ml-auto inline-block text-sm underline" href="#"> Glemt passord? </a>
                -->
              </div>

              <Input id="password" v-model="password" required type="password" />
              <Button class="w-full" type="submit">Logg inn</Button>
              <p v-if="errorMessage" class="text-red-500 font-bold">{{ errorMessage }}</p>
            </div>
          </form>
        </div>

        <div class="mt-4 text-center text-sm">
          Har du ikke bruker?
          <router-link id="signup-link" class="underline" to="/signup"> Registrer her</router-link>
        </div>
      </CardContent>
    </Card>
  </div>
</template>
