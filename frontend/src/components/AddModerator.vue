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
import {DialogClose} from "@/components/ui/dialog/index.js";

</script>

<template>
  <Dialog>
    <DialogTrigger>
      <Button class="w-full bg-black text-white">
        Legg til moderator
      </Button>
    </DialogTrigger>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Legg til moderator</DialogTitle>
        <Input
          placeholder="Navn"
          type="text"
        />
        <Input
          placeholder="E-post"
          type="text"
        />
      </DialogHeader>

      <DialogFooter class="flex flex-col items-center">
        <DialogClose>
          <Button variant="outline" class="w-48">
            Avbryt
          </Button>
        </DialogClose>
        <Button class="w-48" variant="default">
          Legg til
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
