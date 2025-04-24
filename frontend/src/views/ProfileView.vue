<script setup>
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import {Button} from "@/components/ui/button/index.js";
import {Input} from "@/components/ui/input/index.js";
import {DialogClose} from "@/components/ui/dialog/index.js";
import {Label} from "@/components/ui/label/index.js";
</script>

<template>
  <div class="m-auto flex flex-col items-center gap-4">
    <Card class="max-w-sm">
      <CardHeader>
        <CardTitle class="text-2xl"> Min side</CardTitle>
      </CardHeader>
      <CardContent>
        <div class="grid gap-4">
          <div class="grid gap-2">
            <label>
              Personalia:
            </label>
            <Dialog>
              <DialogTrigger>
                <Button class="w-full">Rediger profil</Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>Rediger profil</DialogTitle>
                  <label>
                    Her kan du endre profilen din. Trykk på "Lagre" når du er ferdig.
                  </label>
                  <Input
                    placeholder="[navn]"
                    type="text"
                  />
                  <Input
                    placeholder="[epostadresse]"
                    type="email"
                  />
                  <label>
                    Endre passord?
                  </label>
                  <Input
                    placeholder="Gammelt passord"
                    type="password"
                  />
                  <Input
                    placeholder="Nytt passord"
                    type="password"
                  />
                  <Input
                    placeholder="Gjenta nytt passord"
                    type="password"
                  />
                </DialogHeader>

                <DialogFooter class="flex flex-col items-center">
                  <DialogClose>
                    <Button>Lagre</Button>
                  </DialogClose>
                </DialogFooter>
              </DialogContent>
            </Dialog>
            <label>
              Husstand:
            </label>
            <Button class="w-full">Rediger husstand</Button>
          </div>
        </div>
      </CardContent>
    </Card>
    <Button class="w-full rounded-xl" variant="destructive">Logg ut</Button>
  </div>
</template>

<style scoped>

</style>
