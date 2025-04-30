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
        Send tilbakestill passord-lenke
      </Button>
    </DialogTrigger>
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Send tilbakestill passord-lenke</DialogTitle>
        <Input
          placeholder="Navn"
          type="text"
        />
        <Input
          placeholder="E-post"
          type="text"
        />
      </DialogHeader>

      <DialogFooter class="flex gap-2">
        <DialogClose>
          <Button variant="outline" class="flex-1">
            Avbryt
          </Button>
        </DialogClose>
        <Button class="flex-1" variant="default">
          Send
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
