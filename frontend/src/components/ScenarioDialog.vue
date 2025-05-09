<!-- components/ScenarioDialog.vue -->
<script setup>
import { ref, watch } from 'vue'
import { useScenarioStore } from '@/stores/scenario'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog'

const props = defineProps({
  scenario: {
    type: Object,
    default: null
  }
})

const openDialog = ref(true)
watch(openDialog, (val) => {
  if (!val)
    emit('close')
  })

const emit = defineEmits(['close'])

const scenarioStore = useScenarioStore()

const title = ref(props.scenario?.title || '')
const description = ref(props.scenario?.description || '')
const content = ref(props.scenario?.content || '')
const url = ref(props.scenario?.url || '')

const handleSubmit = async () => {
  try {
    const scenarioData = {
      title: title.value,
      description: description.value,
      content: content.value,
      url: url.value
    }

    if (props.scenario) {
      scenarioData.id = props.scenario.id
      await scenarioStore.updateScenario(scenarioData)
    } else {
      await scenarioStore.createScenario(scenarioData)
    }

    emit('close')
  } catch (error) {
    alert('Error saving scenario: ' + error.message)
  }
}
</script>

<template>
  <Dialog v-model:open="openDialog" @close="emit('close')">
    <DialogContent class="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>
          {{ props.scenario ? 'Endre scenario' : 'Legg til nytt scenario' }}
        </DialogTitle>
      </DialogHeader>

      <div class="grid gap-4 py-4">
        <div class="grid grid-cols-4 items-center gap-4">
          <label class="text-right">Tittel</label>
          <Input v-model="title" class="col-span-3" type="text"/>
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <label class="text-right">Beskrivelse</label>
          <Input v-model="description" class="col-span-3" type="text" />
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <label class="text-right">Innhold</label>
          <textarea
            id="text"
            v-model="content"
            class="col-span-3 border rounded-md"
            rows="4"
          />
        </div>
      </div>

      <DialogFooter>
        <Button variant="outline" @click="emit('close')">Avbryt</Button>
        <Button @click="handleSubmit">Lagre</Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
