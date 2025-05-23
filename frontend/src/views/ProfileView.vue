<script setup>
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from '@/components/ui/dialog'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
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
  AlertDialogTrigger
} from '@/components/ui/alert-dialog'
import { Button } from '@/components/ui/button/index.js'
import { Input } from '@/components/ui/input/index.js'
import { DialogClose } from '@/components/ui/dialog/index.js'
import { Label } from '@/components/ui/label/index.js'
import { computed, onMounted, ref } from 'vue'
import router from '@/router/router.js'
import { useSessionStore } from '@/stores/session'
import { useStorageStore } from '@/stores/storage'
import EditStorage from '@/components/EditStorage.vue'
import axios from 'axios'

const username = ref('')
const email = ref('')
const trackingDeleted = ref(false)

const householdName = ref('')
const address = ref('')
const resolvedAddresses = ref({})

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

  let location = null

  if (address.value.trim()) {
    try {
      const response = await axios.get(
        'https://nominatim.openstreetmap.org/search',
        {
          params: {
            q: address.value,
            countrycodes: 'no',
            format: 'json',
            limit: 1
          },
          headers: {
            'Accept-Language': 'no'
          }
        }
      )

      const result = response.data[0]
      if (result) {
        location = {
          latitude: parseFloat(result.lat),
          longitude: parseFloat(result.lon)
        }
      } else {
        console.warn('Fant ingen lokasjon for adressen')
      }
    } catch (error) {
      console.error('Feil ved henting av koordinater:', error)
    }
  }

  const response = await storageStore.create(householdName.value, token, location)

  if (response) {
    householdName.value = ''
    address.value = ''
  }

  await storageStore.fetchAll(token)
  await loadAddresses()
}


async function joinStorage() {
  if (!joinToken.value) return
  const success = await storageStore.join(joinToken.value, sessionStore.token)
  if (success) {
    joinToken.value = ''
  } else {
    console.error('Could not join storage')
  }

  await storageStore.fetchAll(sessionStore.token)
  await loadAddresses()
}

function openEditProfile() {
  if (user.value) {
    username.value = user.value.name
    email.value = user.value.email
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

async function resolveAddressFromLocation(location) {
  const latitude = location.latitude
  const longitude = location.longitude

  const response = await axios.get(`https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`, {})

  if (response.data.error) throw new Error(response.data.error)

  const address = response.data.address

  const road = address.road || 'Ukjent gate'
  const houseNumber = address.house_number || 'ukjent nummer'
  const postcode = address.postcode || 'ukjent postnummer'
  const city = address.city || address.town || address.village || 'ukjent sted'

  return `${road} ${houseNumber}, ${postcode} ${city}`
}

async function loadAddresses() {
  for (const s of storageStore.storages) {
    if (s.location?.latitude && s.location?.longitude) {
      try {
        const address = await resolveAddressFromLocation(s.location)
        resolvedAddresses.value[s.id] = address
      } catch (err) {
        console.error(`Kunne ikke resolve adresse for storage ${s.id}:`, err)
      }
    }
  }
}

async function changeLocationTracking() {
  if (!user.value) {
    console.warn('Bruker er ikke innlogget – kan ikke endre tracking')
    return
  }
  const current = user.value.trackingEnabled
  const success = await sessionStore.updateLocationTracking(!current)
  if (success) {
    if (user.value) {
      user.value.trackingEnabled = !current
    }
  } else {
    console.error('Kunne ikke oppdatere tracking-preference')
  }
}

async function deleteTrackingHistory() {
  const success = await sessionStore.updateDeletedLocationHistory()
  if (success) {
    trackingDeleted.value = true
  } else {
    console.error('Could not delete tracking history')
  }
}

async function handleStorageUpdated() {
  await storageStore.fetchAll(sessionStore.token)
  await loadAddresses()
}

onMounted(async () => {
  if (!sessionStore.isAuthenticated) {
    router.push('/login')
  }

  if (user.value) {
    username.value = user.value.name
    email.value = user.value.email
  }

  if (trackingDeleted.value === true) {
    trackingDeleted.value = false
  }

  try {
    await storageStore.fetchAll(sessionStore.token)
    await loadAddresses()
  } catch (error) {
    console.error('Could not fetch storages and members:', error)
  }
})
</script>

<template>
  <div class="m-auto flex flex-col items-center gap-4">
    <Card class="mx-5 mt-5">
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
            <p v-if="user && user.email">Epostadresse: {{ user.email }}</p><br />
          </CardDescription>
          <Dialog>
            <DialogTrigger @click="openEditProfile">
              <Button class="w-48" data-testid="edit-profile-btn"
                      test>Rediger personalia
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle class="text-2xl">Rediger profil</DialogTitle>
                <DialogDescription>
                  <Label>Her kan du endre profilen din. Trykk på "Lagre" når du er ferdig.</Label>
                  <div class="flex flex-col gap-2">
                    <Input v-model="username" data-testid="username-input" placeholder="Navn"
                           type="text" />
                    <Input v-model="email" data-testid="email-input" placeholder="Epostadresse"
                           type="email" />
                  </div>
                </DialogDescription>
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
              <Button class="w-48" data-testid="change-passwd-btn">Endre passord</Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle class="text-2xl">Endre passord</DialogTitle>
                <Input v-model="oldPassword" placeholder="Gammelt passord" type="password" />
                <Input v-model="newPassword" placeholder="Nytt passord" type="password" />
                <Input v-model="confirmPassword" placeholder="Gjenta nytt passord"
                       type="password" />
                <p v-if="passwordError" class="text-red-600 font-bold">{{ passwordError }}</p>
                <p v-if="passwordSuccess" class="text-green-600 font-bold">{{ passwordSuccess }}</p>
              </DialogHeader>
              <div class="flex flex-col items-center sm:items-start">
                <Button class="w-48" @click="submitChangePassword">Endre passord</Button>
              </div>
              <DialogFooter>
                <DialogClose>
                  <Button class="w-48">Lukk</Button>
                </DialogClose>
              </DialogFooter>
            </DialogContent>
          </Dialog>
          <br />
          <Label class="text-xl">Husstander:</Label>
          <div class="flex flex-col items-center gap-2">
            <CardDescription>
              <div v-for="s in storages" :key="s.id" class="flex flex-col gap-4 w-full">
                <div class="border p-4 rounded-md shadow-sm w-96 grid gap-2 mt-4">
                  <h3 class="text-xl font-bold">{{ s.name }}</h3>
                  <div v-if="user.id === s.storageOwner">
                    <p>Husstandsnummer: {{ s.token }}</p>
                  </div>
                  <p>Lokasjon: {{ resolvedAddresses[s.id] || 'Ikke angitt.' }}</p>
                  <h4 class="mt-2 font-semibold">Medlemmer:</h4>
                  <ul v-if="membersByStorageId[s.id]">
                    <li v-for="(member, index) in membersByStorageId[s.id]" :key="index">
                      {{ member.name }}
                    </li>
                  </ul>
                  <p v-else>Laster medlemmer...</p>
                  <div v-if="user.id === s.storageOwner">
                    <EditStorage :storage="s" @updated="handleStorageUpdated" />
                  </div>
                  <div v-else class="flex flex-col items-center gap-4">
                    <AlertDialog>
                      <AlertDialogTrigger as-child>
                        <Button class="w-48">Forlat husstand</Button>
                      </AlertDialogTrigger>
                      <AlertDialogContent>
                        <AlertDialogHeader>
                          <AlertDialogTitle>Er du sikker?</AlertDialogTitle>
                          <AlertDialogDescription>
                            <Label>
                              Er du sikker på at du ønsker å forlate husstanden? Dette kan ikke
                              angres.
                            </Label>
                          </AlertDialogDescription>
                        </AlertDialogHeader>
                        <AlertDialogFooter>
                          <AlertDialogCancel>Nei</AlertDialogCancel>
                          <AlertDialogAction @click="removeUser(user.id, s.id)">Ja
                          </AlertDialogAction>
                        </AlertDialogFooter>
                      </AlertDialogContent>
                    </AlertDialog>
                  </div>
                </div>
              </div>
            </CardDescription>
            <br />
            <Dialog>
              <DialogTrigger>
                <Button class="w-48" data-testid="create-storage-btn">Opprett ny husstand</Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle class="text-2xl">Opprett ny husstand</DialogTitle>
                  <Input
                    v-model="householdName"
                    placeholder="Husstandsnavn"
                    type="text"
                  />
                  <Input
                    v-model="address"
                    placeholder="Adresse (valgfritt)"
                    type="text"
                  />
                  <DialogClose>
                    <Button class="w-48 mt-4" @click="createNewStorage()">Opprett</Button>
                  </DialogClose>
                </DialogHeader>
              </DialogContent>
            </Dialog>
            <div class="flex flex-col items-center gap-4 mt-6">
              <Label>Skriv inn husstandsnummer for å bli med i en annen husstand:</Label>
              <Input v-model="joinToken" class="w-48" placeholder="Husstandsnummer" />
              <Button class="w-48" data-testid="join-storage-btn" @click="joinStorage">Bli med i
                husstand
              </Button>
            </div>
          </div>
          <br />
          <Label class="text-xl">Personvern:</Label>
          <CardDescription>
            <div class="flex flex-col items-center text-center gap-4">
              <div v-if="user?.trackingEnabled" class="flex flex-col items-center gap-4">
                <div class="text-left">
                  <Label>Du deler lokasjonen din med Krisefikser. Ønsker du å skru av
                    stedstjenester?</Label>
                </div>
                <Button class="w-48" @click="changeLocationTracking">Skru av</Button>
              </div>

              <div v-else class="flex flex-col items-center gap-4">
                <div class="text-left">
                  <Label>Du deler ikke lokasjonen din med Krisefikser. Ønsker du å skru på
                    stedstjenester?</Label>
                </div>
                <Button class="w-48" @click="changeLocationTracking">Skru på</Button>
              </div>
            </div>
          </CardDescription>


          <CardDescription>
            <div class="flex flex-col items-center text-center gap-4">
              <span class="text-left">
                <Label>Ønsker du å slette din lokasjonshistorikk?</Label>
                <Label> Trykk på knappen under for å slette lokasjonshistorikk.</Label>
              </span>
              <AlertDialog>
                <AlertDialogTrigger>
                  <Button class="w-48" data-testid="delete-location-history-btn">Slett
                    lokasjonshistorikk
                  </Button>
                </AlertDialogTrigger>
                <AlertDialogContent>
                  <AlertDialogHeader>
                    <AlertDialogTitle class="text-2xl">Slett lokasjonshistorikk</AlertDialogTitle>
                    <AlertDialogDescription>
                      Er du sikker på at du vil slette din lokasjonshistorikk? Dette kan ikke
                      angres.
                    </AlertDialogDescription>
                  </AlertDialogHeader>
                  <AlertDialogFooter>
                    <AlertDialogCancel>Avbryt</AlertDialogCancel>
                    <AlertDialogAction class="bg-red-500 text-white" @click="deleteTrackingHistory">
                      Slett
                    </AlertDialogAction>
                  </AlertDialogFooter>
                </AlertDialogContent>
              </AlertDialog>
              <div>
                <Label v-if="trackingDeleted === true" class="text-green-600 font-bold">Lokasjonshistorikk
                  slettet!</Label>
              </div>
            </div>
          </CardDescription>
        </div>
      </CardContent>
    </Card>
    <router-link to="/login">
      <Button class="w-48" data-testid="logout-btn" variant="destructive"
              @click="sessionStore.logout">Logg ut
      </Button>
    </router-link>
  </div>
</template>

<style scoped>

</style>
