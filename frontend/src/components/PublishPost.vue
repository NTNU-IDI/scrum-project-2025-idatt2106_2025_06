<script setup>
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'

import {Button} from "@/components/ui/button/index.js";
import {Input} from "@/components/ui/input/index.js";
import {Label} from "@/components/ui/label/index.js";
import {DialogClose} from "@/components/ui/dialog/index.js";

</script>

<template>
  <Dialog>
    <DialogTrigger>
      <Button class="w-48 bg-black text-white">
        Legg til innlegg
      </Button>
    </DialogTrigger>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Legg til innlegg</DialogTitle>
        <label>
          Her kan du legge til et nytt innlegg. Trykk på "Publiser" når du er ferdig.
        </label>
        <Input
          placeholder="Tittel"
          type="text"
        />
        <Input
          placeholder="Kort beskrivelse"
          type="text"
        />
        <label>
          Innhold:
        </label>
        <Input
          placeholder="Skriv her..."
          rows="4"
          class="w-full rounded-md border p-2 text-sm"
        />
      </DialogHeader>

      <DialogFooter class="flex flex-col items-center">
        <DialogClose>
          <Button variant="outline" class="w-48">
            Avbryt
          </Button>
        </DialogClose>
        <Button class="w-full" variant="default">
          Publiser
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
