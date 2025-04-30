<script setup>
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from '@/components/ui/alert-dialog'
import {Button} from "@/components/ui/button/index.js";
import {Input} from "@/components/ui/input/index.js";
import {DialogClose} from "@/components/ui/dialog/index.js";
import {Label} from "@/components/ui/label/index.js";
import { computed, onMounted, ref } from 'vue'
import router from '@/router/index.js'
import { useSessionStore } from '@/stores/session'
import { useStorageStore } from '@/stores/storage'
import EditStorage from '@/components/EditStorage.vue'

const username = ref('');
const email = ref('');

const householdName = ref('');
const location = ref('');

const joinToken = ref('')

const sessionStore = useSessionStore()
const user = computed(() => sessionStore.user)

const storageStore = useStorageStore()
const storages = computed(() => storageStore.storages)
const membersByStorageId = computed(() => storageStore.membersByStorageId)

async function createNewStorage() {
  const token = sessionStore.token

  const response = await storageStore.create(
    householdName.value, token)

  if (response) {
    console.log("Husstand opprettet")
  }
}

async function joinStorage() {
  if (!joinToken.value) return

  const success = await storageStore.join(joinToken.value, sessionStore.token)

  if (success) {
    console.log('Bli med i husstand: Vellykket')
    joinToken.value = ''
  } else {
    console.error('Kunne ikke bli med i husstand')
  }
}


onMounted(async () => {
  if (!sessionStore.isAuthenticated) {
    router.push('/login')
    console.log("Det er noe galt med innloggingen");
  }

  if (user.value) {
    username.value = user.value.name
    email.value = user.value.email
  }

  try {
    await storageStore.fetchAll(sessionStore.token)
  } catch (error) {
    console.error("Klarte ikke hente husstander og medlemmer:", error)
  }
});

function openEditProfile() {
  if (user.value) {
    username.value = user.value.name;
    email.value = user.value.email;
  }
}
</script>

<template>
  <div class="m-auto flex flex-col items-center gap-4">
    <Card class="">
      <CardHeader>
        <CardTitle class="text-2xl w-96"> Min side</CardTitle>
      </CardHeader>
      <CardContent>
        <div class="grid gap-2">
          <Label>
            Personalia:
          </Label>
          <CardDescription>
            <p v-if="user && user.name">Brukernavn: {{ user.name }}</p>
            <p v-if="user && user.email">Epostadresse: {{ user.email }}</p><br/>
          </CardDescription>
          <Dialog>
            <DialogTrigger @click="openEditProfile">
              <Button class="w-48">Rediger profil</Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle class="text-2xl">Rediger profil</DialogTitle>
                <Label>
                  Her kan du endre profilen din. Trykk på "Lagre" når du er ferdig.
                </Label>
                <Input
                  v-model="username"
                  placeholder="Navn"
                  type="text"
                />
                <Input
                  v-model="email"
                  placeholder="Epostadresse"
                  type="email"
                /><br/>
                <Label>
                  Endre passord?
                </Label>
                <Input
                  placeholder="Gammelt passord"
                  type="password"
                />
                <Input
                  placeholder="Nytt passord"
                  type="password"
                />
                <Input
                  placeholder="Gjenta nytt passord"
                  type="password"
                />
              </DialogHeader>
              <DialogFooter class="flex flex-col items-center">
                <DialogClose>
                  <Button class="w-48">Lagre</Button>
                </DialogClose>
              </DialogFooter>
            </DialogContent>
          </Dialog>
          <br/>
          <Label>Husstander:</Label>
          <div class="flex flex-col items-center gap-2">
              <CardDescription>
                <div v-for="s in storages" :key="s.id" class="flex flex-col gap-4 w-full">
                  <div class="border p-4 rounded-md shadow-sm w-full grid gap-2 mt-4">
                    <h3 class="text-xl font-bold">{{ s.name }}</h3>
                    <p>Husstandsnummer: {{ s.token }}</p>
                    <h4 class="mt-2 font-semibold">Medlemmer:</h4>
                    <ul v-if="membersByStorageId[s.id]">
                      <li v-for="(member, index) in membersByStorageId[s.id]" :key="index">
                        {{ member }}
                      </li>
                    </ul>
                    <p v-else>Laster medlemmer...</p>
                    <EditStorage :storage="s" />
                  </div>
                </div>
              </CardDescription>
            <Dialog>
              <DialogTrigger>
                <Button class="w-48">Opprett husstand</Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle class="text-2xl">Opprett husstand</DialogTitle>
                  <Input
                    v-model="householdName"
                    placeholder="Husstandsnavn"
                    type="text"
                  />
                  <Input
                    v-model="location"
                    placeholder="Lokasjon (valgfritt)"
                    type="text"
                  />
                  <Button @click="createNewStorage()" class="w-48">Opprett</Button>
                </DialogHeader>
              </DialogContent>
            </Dialog>
            <div class="flex flex-col items-center gap-4 mt-8">
              <Label>Skriv inn husstandsnummer for å bli med i en husstand:</Label>
              <Input v-model="joinToken" placeholder="Husstandsnummer" class="w-64" />
              <Button class="w-48" @click="joinStorage">Bli med i husstand</Button>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
    <router-link to="/login">
      <Button @click="sessionStore.logout" class="w-48" variant="destructive">Logg ut</Button>
    </router-link>
  </div>
</template>

<style scoped>

</style>
