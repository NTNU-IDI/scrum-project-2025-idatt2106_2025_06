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
        Legg til en varsling
      </Button>
    </DialogTrigger>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Publiser varsling</DialogTitle>

        <div class="flex items-center">
          <Label class="m-2 w-24" for="title">Navn:</Label>
          <Input class="border w-full" id="title" placeholder="Navn på varsel" />
        </div>

        <div class="flex items-center">
          <Label class="m-2 w-24" for="text">Varsel:</Label>
          <Input class="border w-full" id="text" placeholder="Innhold for varsel" />
        </div>

        <div class="flex items-center">
          <Label class="m-2 w-24" for="type">Type:</Label>
          <Input class="border w-full" id="type" placeholder="Type varsel" />
        </div>

        <div class="flex items-center">
          <Label class="m-2 w-20" for="date">Tidspunkt:</Label>
          <Input type="date" id="date" v-model="date" class="w-fit" />
          <Input type="time" id="date" v-model="time" class="w-fit" />
        </div>
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
