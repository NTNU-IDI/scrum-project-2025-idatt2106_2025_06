<script setup>

import {
  AlertDialog,
  AlertDialogAction, AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle,
  AlertDialogTrigger
} from '@/components/ui/alert-dialog/index.js'
import { Button } from '@/components/ui/button/index.js'
import {
  Dialog, DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader, DialogTitle,
  DialogTrigger
} from '@/components/ui/dialog/index.js'
import { Input } from '@/components/ui/input/index.js'
import { Label } from '@/components/ui/label/index.js'
import { UserMinus } from 'lucide-vue-next'
import { computed, ref } from 'vue'
import { useSessionStore } from '@/stores/session'
import { useStorageStore } from '@/stores/storage'
import axios from 'axios'

const props = defineProps({
  storage: Object
})

const sessionStore = useSessionStore()
const user = computed(() => sessionStore.user)
const storageStore = useStorageStore()

const membersByStorageId = computed(() => storageStore.membersByStorageId)

const name = ref(props.storage.name)
const addressText = ref('')
const location = ref(null)

const emit = defineEmits(['updated'])

async function saveChanges() {
  if (addressText.value.trim()) {
    try {
      const response = await axios.get(
        'https://nominatim.openstreetmap.org/search',
        {
          params: {
            q: addressText.value,
            countrycodes: 'no',
            format: 'json',
            limit: 1,
          },
          headers: {
            'Accept-Language': 'no',
          },
        }
      );

      const result = response.data[0];
      if (result) {
        location.value = {
          latitude: parseFloat(result.lat),
          longitude: parseFloat(result.lon),
        };
      } else {
        console.warn('Fant ingen lokasjon for adressen');
      }
    } catch (error) {
      console.error('Feil ved henting av koordinater:', error);
    }
  }

  try {
    await storageStore.edit(props.storage.id, name.value, location.value, sessionStore.token)
    emit('updated')
  } catch (err) {
    console.error('Feil ved oppdatering av husstand:', err)
  }
}

async function removeUser(userId) {
  try {
    const success = await storageStore.removeMember(userId, props.storage.id, sessionStore.token)
    if (success) {
      console.log('Medlem fjernet')
    } else {
      console.error('Klarte ikke fjerne medlem')
    }
  } catch (e) {
    console.error('Feil ved fjerning:', e)
  }
}
</script>

<template>
  <div class="flex flex-col items-center">
    <Dialog>
      <DialogTrigger>
        <Button class="w-48">Endre husstand</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle class="text-2xl">Endre husstand</DialogTitle>
          <Label>
            Her kan du endre husstanden din. Trykk på "Lagre" når du er ferdig.
          </Label>
          <Input
            v-model="name"
            placeholder="Husstandsnavn"
            type="text"
          />
          <Input
            v-model="addressText"
            placeholder="Lokasjon (valgfritt)"
            type="text"
          /><br/>
          <DialogTitle>Medlemmer</DialogTitle>
          <div>
            <ul v-if="membersByStorageId[props.storage.id]" class="flex flex-col gap-2">
              <li
                v-for="(member, index) in membersByStorageId[props.storage.id]"
                :key="index"
                class="flex flex-row justify-between items-center w-full">
                <span class="text-left">{{ member.name }}</span>
                <AlertDialog>
                  <AlertDialogTrigger as-child >
                    <div v-if="user && user.id !== member.id">
                      <Button size="icon" variant="outline">
                        <user-minus/>
                      </Button>
                    </div>
                  </AlertDialogTrigger>
                  <AlertDialogContent>
                    <AlertDialogHeader>
                      <AlertDialogTitle>Er du sikker?</AlertDialogTitle>
                      <AlertDialogDescription>
                        Er du sikker på at du ønsker å fjerne medlemmet fra husstanden?
                      </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                      <AlertDialogCancel>Nei</AlertDialogCancel>
                      <AlertDialogAction @click="removeUser(member.id)">Ja</AlertDialogAction>
                    </AlertDialogFooter>
                  </AlertDialogContent>
                </AlertDialog>
              </li>
            </ul>
          </div>
        </DialogHeader>
        <DialogFooter class="flex flex-col items-center">
          <div class="flex flex-col items-center gap-2 w-full">
            <DialogClose as-child>
              <Button class="w-48" @click="saveChanges">Lagre</Button>
            </DialogClose>
            <AlertDialog>
              <AlertDialogTrigger as-child>
                <Button class="w-48" variant="destructive">Slett lager</Button>
              </AlertDialogTrigger>
              <AlertDialogContent>
                <AlertDialogHeader>
                  <AlertDialogTitle>Er du sikker?</AlertDialogTitle>
                  <AlertDialogDescription>
                    Er du sikker på at du ønsker å slette husstanden? Dette kan ikke angres.
                  </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                  <AlertDialogCancel>Nei</AlertDialogCancel>
                  <AlertDialogAction @click="">Ja</AlertDialogAction>
                </AlertDialogFooter>
              </AlertDialogContent>
            </AlertDialog>
          </div>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  </div>
</template>

<style scoped>

</style>
