<script setup>
import { ref, onMounted } from 'vue'
import { fetchModerators, createModerator, removeModerator } from '@/service/moderatorService'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'

const moderators = ref([])
const newModerator = ref('')

const token = sessionStorage.getItem('token')

onMounted(async () => {
  if (!token) return
  moderators.value = await fetchModerators(token)
})

const handleAdd = async () => {
  if (!newModerator.value || !token ) return
  await createModerator(newModerator.value)
  moderators.value = await fetchModerators()
  newModerator.value = ''
}

const handleRemove = async (email) => {
  if (!token) return
  await removeModerator(email, token)
  moderators.value = await fetchModerators()
}
</script>

<template>
  <Card class="max-w-lg mx-auto mt-10 p-6">
    <CardHeader>
      <CardTitle>Administrer moderatorer</CardTitle>
    </CardHeader>
    <CardContent class="flex flex-col gap-4">
      <div class="flex items-center gap-2">
        <Label for="email" class="w-24">E-post:</Label>
        <Input id="email" v-model="newModerator" placeholder="ny@epost.no" class="flex-1" />
        <Button @click="handleAdd">Legg til</Button>
      </div>

      ```
      <div class="mt-4">
        <h3 class="font-semibold mb-2">Moderatorer</h3>
        <ul class="flex flex-col gap-2">
          <li v-for="mod in moderators" :key="mod">
            <div class="flex justify-between items-center border rounded p-2">
              <span>{{ mod }}</span>
              <Button variant="destructive" @click="handleRemove(mod)">Fjern</Button>
            </div>
          </li>
        </ul>
      </div>
    </CardContent>
    ```

  </Card>
</template>
