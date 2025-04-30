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
import {X, UserMinus} from 'lucide-vue-next';
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import router from '@/router/index.js'
import axios from 'axios'
import { useSessionStore } from '@/stores/session'
import { useStorageStore } from '@/stores/storage'
import EditStorage from '@/components/EditStorage.vue'
import { createStorage } from '@/service/storageService.js'

const username = ref('');
const email = ref('');
const householdName = ref('');
const householdNumber = ref('');
const location = ref('');

const session = useSessionStore()
const user = computed(() => session.user)

const storage = useStorageStore()
const household = computed(() => storage.household)

const storages = computed(() => storage.storages)
const memberByStorageId = computed(() => storage.memberByStorageId)

async function createNewStorage() {
  const token = session.token
  const response = await storage.create(
    householdName.value, token)

  if (response) {
    console.log("Husstand opprettet")
  }
}


onMounted(async () => {
  if (!session.isAuthenticated) {
    router.push('/login')
    console.log("Det er noe galt med innloggingen");
  }

  if (user.value) {
    username.value = user.value.name
    email.value = user.value.email
  }

  try {
    await storage.fetchAll(session.token)
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

          <div class="flex flex-col items-center gap-2">
            <Label>
              Du er ikke registrert i en husstand.
            </Label>
              <Label>Husstand:</Label>
              <CardDescription>
                <div v-for="s in storages" :key="s.id" class="border p-4 rounded-md shadow-sm w-full">
                  <h3 class="text-xl font-bold">{{ s.name }}</h3>
                  <p>Token: {{ s.token }}</p>
                  <p>ID: {{ s.id }}</p>

                  <h4 class="mt-2 font-semibold">Medlemmer:</h4>
                  <ul>
                    <li v-for="member in membersByStorageId[s.id]" :key="member.id">
                      {{ member.name }} ({{ member.email }})
                    </li>
                  </ul>
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
          </div>

          <!--
          <div>
            <Label>
              Husstand:
            </Label>
            <CardDescription>
              <p>Husstandsnavn: {{ householdName }}</p>
              <p>Lokasjon: {{ location === null ? 'Ikke spesifisert' : location }}</p>
              <p>Husstandsnummer: {{ householdNumber }}</p>
              <br/>
              [Medlem 1]<br/>
              [Medlem 2]<br/>
            </CardDescription>
            <div>
              <EditStorage/>
            </div>
          </div>
          -->

        </div>
      </CardContent>
    </Card>
    <router-link to="/login">
      <Button @click="session.logout" class="w-48" variant="destructive">Logg ut</Button>
    </router-link>
  </div>
</template>

<style scoped>

</style>
