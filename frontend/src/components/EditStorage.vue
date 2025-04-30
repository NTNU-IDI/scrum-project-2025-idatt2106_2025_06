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

const props = defineProps({
  storage: Object
})

const sessionStore = useSessionStore()
const storageStore = useStorageStore()

const membersByStorageId = computed(() => storageStore.membersByStorageId)

const name = ref(props.storage.name)
const location = ref(props.storage.location ?? '')

async function saveChanges() {
  try {
    await storageStore.editStorage(props.storage.id, name.value, location.value, sessionStore.token)
    console.log('Husstand oppdatert')
  } catch (err) {
    console.error('Feil ved oppdatering av husstand:', err)
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
            v-model="location"
            placeholder="Lokasjon (valgfritt)"
            type="text"
          /><br/>
          <DialogTitle>Medlemmer</DialogTitle>
          <div class="flex items-center gap-2 justify-between">
            <ul v-if="membersByStorageId[props.storage.id]">
              <li v-for="(member, index) in membersByStorageId[props.storage.id]" :key="index">
                {{ member }}
              </li>
            </ul>
            <AlertDialog>
              <AlertDialogTrigger as-child>
                <Button size="icon" variant="outline">
                  <user-minus/>
                </Button>
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
                  <AlertDialogAction>Ja</AlertDialogAction>
                </AlertDialogFooter>
              </AlertDialogContent>
            </AlertDialog>
          </div>
        </DialogHeader>
        <DialogFooter class="flex flex-col items-center">
          <DialogClose>
            <Button class="w-48" @click="saveChanges">Lagre</Button>
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  </div>
</template>

<style scoped>

</style>
