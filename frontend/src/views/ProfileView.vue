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
import router from '@/router/router.js'
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

const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const passwordError = ref('')
const passwordSuccess = ref('')

async function createNewStorage() {
  const token = sessionStore.token
  const response = await storageStore.create(
    householdName.value, token)
  if (response) {
  }
}

async function joinStorage() {
  if (!joinToken.value) return
  const success = await storageStore.join(joinToken.value, sessionStore.token)
  if (success) {
    joinToken.value = ''
  } else {
    console.error('Could not join storage')
  }
}

function openEditProfile() {
  if (user.value) {
    username.value = user.value.name;
    email.value = user.value.email;
  }
}

async function submitProfileUpdate() {
  const success = await sessionStore.updateProfile(username.value, email.value)
  if (success) {

    await storageStore.fetchAll(sessionStore.token)
  }
}

async function submitChangePassword() {
  passwordError.value = ''
  passwordSuccess.value = ''

  if (newPassword.value !== confirmPassword.value) {
    passwordError.value = 'Passwords do not match.'
    return
  }

  const success = await sessionStore.updatePassword(
    oldPassword.value,
    newPassword.value,
    confirmPassword.value
  )

  if (success) {
    passwordSuccess.value = 'Passord endret!'
    oldPassword.value = ''
    newPassword.value = ''
    confirmPassword.value = ''
  } else {
    passwordError.value = 'Kunne ikke endre passord.'
  }
}

async function removeUser(userId, storageId) {
  try {
    const success = await storageStore.removeMember(userId, storageId, sessionStore.token)
    if (success) {
      console.log('Medlem fjernet')
    } else {
      console.error('Klarte ikke fjerne medlem')
    }
  } catch (e) {
    console.error('Feil ved fjerning:', e)
  }
}

onMounted(async () => {
  if (!sessionStore.isAuthenticated) {
    router.push('/login')
  }

  if (user.value) {
    username.value = user.value.name
    email.value = user.value.email
  }

  try {
    await storageStore.fetchAll(sessionStore.token)
  } catch (error) {
    console.error("Could not fetch storages and members:", error)
  }
});
</script>

<template>
  <div class="m-auto flex flex-col items-center gap-4">
    <Card class="">
      <CardHeader>
        <CardTitle class="text-2xl w-96"> Min side</CardTitle>
      </CardHeader>
      <CardContent>
        <div class="grid gap-2">
          <Label class="text-xl">
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
                <Label>Her kan du endre profilen din. Trykk på "Lagre" når du er ferdig.</Label>
                <Input v-model="username" placeholder="Navn" type="text" />
                <Input v-model="email" placeholder="Epostadresse" type="email" />
              </DialogHeader>
              <DialogFooter class="flex flex-col items-center">
                <DialogClose>
                  <Button class="w-48" @click="submitProfileUpdate">Lagre</Button>
                </DialogClose>
              </DialogFooter>
            </DialogContent>
          </Dialog>
          <Dialog>
            <DialogTrigger>
              <Button class="w-48">Endre passord</Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle class="text-2xl">Endre passord</DialogTitle>
                <Input v-model="oldPassword" placeholder="Gammelt passord" type="password" />
                <Input v-model="newPassword" placeholder="Nytt passord" type="password" />
                <Input v-model="confirmPassword" placeholder="Gjenta nytt passord" type="password" />
                <p v-if="passwordError" class="text-red-600 font-bold">{{ passwordError }}</p>
                <p v-if="passwordSuccess" class="text-green-600 font-bold">{{ passwordSuccess }}</p>
              </DialogHeader>
              <div class="flex flex-col items-center">
                <Button class="w-48" @click="submitChangePassword">Endre passord</Button>
              </div>
              <DialogFooter class="flex flex-col items-center">
                <DialogClose>
                  <Button class="w-48">Lukk</Button>
                </DialogClose>
              </DialogFooter>
            </DialogContent>
          </Dialog>
          <br/>
          <Label class="text-xl">Husstander:</Label>
          <div class="flex flex-col items-center gap-2">
              <CardDescription>
                <div v-for="s in storages" :key="s.id" class="flex flex-col gap-4 w-full">
                  <div class="border p-4 rounded-md shadow-sm w-96 grid gap-2 mt-4">
                    <h3 class="text-xl font-bold">{{ s.name }}</h3>
                    <p>Husstandsnummer: {{ s.token }}</p>
                    <p>Lokasjon: {{ s.location != null ? s.location : 'Ikke angitt' }}</p>
                    <h4 class="mt-2 font-semibold">Medlemmer:</h4>
                    <ul v-if="membersByStorageId[s.id]">
                      <li v-for="(member, index) in membersByStorageId[s.id]" :key="index">
                        {{ member.name }}
                      </li>
                    </ul>
                    <p v-else>Laster medlemmer...</p>
                    <div v-if="user.id === s.storageOwner">
                      <EditStorage :storage="s" />
                    </div>
                    <div v-else class="flex flex-col items-center gap-4">
                      <AlertDialog>
                        <AlertDialogTrigger as-child >
                            <Button class="w-48">Forlat husstand</Button>
                        </AlertDialogTrigger>
                        <AlertDialogContent>
                          <AlertDialogHeader>
                            <AlertDialogTitle>Er du sikker?</AlertDialogTitle>
                            <AlertDialogDescription>
                              <Label>
                                Er du sikker på at du ønsker å forlate husstanden? Dette kan ikke angres.
                              </Label>
                            </AlertDialogDescription>
                          </AlertDialogHeader>
                          <AlertDialogFooter>
                            <AlertDialogCancel>Nei</AlertDialogCancel>
                            <AlertDialogAction @click="removeUser(user.id, s.id)">Ja</AlertDialogAction>
                          </AlertDialogFooter>
                        </AlertDialogContent>
                      </AlertDialog>
                    </div>
                  </div>
                </div>
              </CardDescription>
            <br/>
            <Dialog>
              <DialogTrigger>
                <Button class="w-48">Opprett ny husstand</Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle class="text-2xl">Opprett ny husstand</DialogTitle>
                  <Input
                    v-model="householdName"
                    placeholder="Husstandsnavn"
                    type="text"
                  />
                  <!--
                  <Input
                    v-model="location"
                    placeholder="Lokasjon (valgfritt)"
                    type="text"
                  />
                  -->
                  <DialogClose>
                    <Button @click="createNewStorage()" class="w-48">Opprett</Button>
                  </DialogClose>
                </DialogHeader>
              </DialogContent>
            </Dialog>
            <div class="flex flex-col items-center gap-4 mt-6">
              <Label>Skriv inn husstandsnummer for å bli med i en annen husstand:</Label>
              <Input v-model="joinToken" placeholder="Husstandsnummer" class="w-48" />
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
