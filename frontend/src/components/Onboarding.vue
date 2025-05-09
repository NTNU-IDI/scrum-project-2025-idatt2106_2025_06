<script setup>
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { ref } from 'vue'
import { ArrowRight } from 'lucide-vue-next'

const step = ref(1)
const isOpen = ref(false)

const stepContent = [
  {
    title: 'Velkommen til Krisefikser!',
    description:
      'Krisefikser hjelper deg med å forberede deg på og å håndtere krisesituasjoner effektivt. Utforsk verktøyene og ressursene vi tilbyr.',
  },
  {
    title: 'Krisescenarioer',
    description: 'Lær om ulike krisescenarioer og hvordan du kan forberede deg på dem. ',
  },
  {
    title: 'Varsler og innlegg',
    description:
      'Hold deg oppdatert på kriser i landet gjennom varslinger og innlegg. Få informasjon i sanntid.',
  },
  {
    title: 'Beredskapslager',
    description:
      'Administrer og hold oversikt over ditt beredskapslager. Sørg for at du alltid er forberedt.',
  },
  {
    title: 'Kom i gang!',
    description:
      'Utforsk Krisefikser og begynn å bruke verktøyene våre for å sikre en tryggere hverdag.',
  },
]

const totalSteps = stepContent.length

const handleContinue = () => {
  if (step.value < totalSteps) {
    step.value++
  }
}

const handleDialogChange = (value) => {
  isOpen.value = value
  if (value) step.value = 1
}
</script>

<template>
  <Dialog :open="isOpen" @update:open="handleDialogChange">
    <DialogTrigger asChild>
      <p class="text-neutral-500 cursor-pointer hover:text-black">Onboarding</p>
    </DialogTrigger>

    <DialogContent class="gap-0 p-0 [&>button:last-child]:text-white">
      <div class="p-2">
        <img
          alt="dialog"
          class="w-full rounded-lg p-20"
          height="216"
          src="/krisefikserLogo.svg"
          width="382"
        />
      </div>
      <div class="space-y-6 px-6 pb-6 pt-3">
        <DialogHeader>
          <DialogTitle>{{ stepContent[step - 1].title }}</DialogTitle>
          <DialogDescription>{{ stepContent[step - 1].description }}</DialogDescription>
        </DialogHeader>

        <div class="flex flex-col justify-between gap-4 sm:flex-row sm:items-center">
          <div class="flex justify-center space-x-1.5 max-sm:order-1">
            <div
              v-for="(_, index) in totalSteps"
              :key="index"
              :class="[
                'h-1.5 w-1.5 rounded-full',
                step === index + 1 ? 'bg-primary' : 'bg-primary opacity-20',
              ]"
            />
          </div>
          <DialogFooter>
            <DialogClose asChild>
              <Button type="button" variant="ghost">Skip</Button>
            </DialogClose>

            <Button v-if="step < totalSteps" class="group" type="button" @click="handleContinue">
              Next
              <ArrowRight
                :size="16"
                :stroke-width="2"
                aria-hidden="true"
                class="-me-1 ms-2 opacity-60 transition-transform group-hover:translate-x-0.5"
              />
            </Button>

            <DialogClose v-else asChild>
              <Button type="button">Okay</Button>
            </DialogClose>
          </DialogFooter>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>
