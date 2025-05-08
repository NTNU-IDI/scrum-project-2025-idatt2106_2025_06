<script setup>
import { ref, onMounted } from 'vue'
import { fetchModerators, createModerator, removeModerator } from '@/mock/moderatorApiMock'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { useModeratorStore } from '@/stores/moderator.js'

const moderatorStore = useModeratorStore()

const newName = ref('')
const newUsername = ref('')
const newEmail = ref('')


//const token = sessionStorage.getItem('token') usikker på om denne skal være med

onMounted(async () => {
  moderatorStore.fetchAll()
})

const handleAdd = async () => {
  if (!newName.value || !newEmail.value || !newUsername.value) {
    alert("Fyll inn begge felt")
    return
  }

  await moderatorStore.add(newName.value, newEmail.value, newName.value)
  newName.value = ''
  newEmail.value = ''
  newUsername.value = ''
  alert('Moderator fjernet!')
}

const handleRemove = async (mod) => {
  await moderatorStore.remove(mod.name, mod.email, mod.id, mod.username)
  alert('Moderator fjernet!')
}

</script>

<template>
  <Card class="max-w-lg mx-auto mt-10 p-6">
    <CardHeader>
      <CardTitle>Administrer moderatorer</CardTitle>
    </CardHeader>
    <CardContent class="flex flex-col gap-4">
      <div class="flex items-center gap-2">
        <Label for="name" class="w-24">Name:</Label>
        <Input id="name" v-model="newName" placeholder="Fornavn Etternavn" class="flex-1" />
      </div>

      <div class="flex items-center gap-2">
        <Label for="username" class="w-24">Brukernavn:</Label>
        <Input id="username" v-model="newUsername" placeholder="Brukernavn" class="flex-1" />
      </div>

      <div class="flex items-center gap-2">
        <Label for="email" class="w-24">E-post:</Label>
        <Input id="email" v-model="newEmail" placeholder="eksempel@hotmail.com" class="flex-1" />
        <Button @click="handleAdd">Legg til</Button>
      </div>

      <div class="mt-4">
        <h3 class="font-semibold mb-2">Moderatorer</h3>
        <ul class="flex flex-col gap-2">
          <li v-for="mod in moderatorStore.moderators" :key="mod.id">
            <div class="flex justify-between items-center border rounded p-4">
              <div class="flex flex-col text-left">
                <span><strong>Navn:</strong> {{ mod.name }}</span>
                <span><strong>Brukernavn:</strong> {{ mod.username }}</span>
                <span><strong>E-post:</strong> {{ mod.email }}</span>
              </div>
              <Button variant="destructive" @click="handleRemove(mod)">Fjern</Button>
            </div>
          </li>
        </ul>
      </div>
    </CardContent>

  </Card>
</template>
