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
      <Button class="w-full bg-black text-white">
        Legg til innlegg
      </Button>
    </DialogTrigger>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Legg til innlegg</DialogTitle>
        <div class="flex align-middle">
          <Label class="m-2" for="name">Navn: </Label>
          <Input class="border w-full" id="name" placeholder="Navn på varsel" />
        </div>
        <div class="flex align-middle">
          <Label class="m-2" for="text">Varsel: </Label>
          <Input class="border w-full" id="test" placeholder="Innhold for varsel" />
        </div>
        <div class="flex align-middle">
          <Label class="m-2" for="text">Type: </Label>
          <Input class="border w-full" id="test" placeholder="Innhold for varsel" />
        </div>
        <Label>
          Her kan du legge til et nytt innlegg. Trykk på "Publiser" når du er ferdig.
        </Label>
        <Input
          placeholder="Tittel"
          type="text"
        />
        <Input
          placeholder="Kort beskrivelse"
          type="text"
        />
        <Label>
          Innhold:
        </Label>
        <Input
          placeholder="Skriv her..."
          rows="4"
          class="w-full rounded-md border p-2 text-sm"
        />
      </DialogHeader>

      <DialogFooter class="flex gap-2">
        <DialogClose>
          <Button variant="outline" class="flex-1">
            Avbryt
          </Button>
        </DialogClose>
        <Button class="flex-1" variant="default">
          Publiser
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
