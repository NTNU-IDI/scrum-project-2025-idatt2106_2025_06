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
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from '@/components/ui/alert-dialog'
import {Button} from "@/components/ui/button/index.js";
import {Input} from "@/components/ui/input/index.js";
import {DialogClose} from "@/components/ui/dialog/index.js";
import {Label} from "@/components/ui/label/index.js";
import {X, UserMinus} from 'lucide-vue-next';

const username = 'username';
const email = 'email';
const householdName = 'householdname';
const householdNumber = 'householdnumber';
const location = null;
</script>

<template>
  <div class="m-auto flex flex-col items-center gap-4">
    <Card class="">
      <CardHeader>
        <CardTitle class="text-2xl w-96"> Min side</CardTitle>
      </CardHeader>
      <CardContent>
        <div class="grid gap-2">
          <label>
            Personalia:
          </label>
          <CardDescription>
            <p>Brukernavn: {{ username }}</p>
            <p>Epostadresse: {{ email }}</p><br/>
          </CardDescription>
          <Dialog>
            <DialogTrigger>
              <Button class="w-48">Rediger profil</Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle class="text-2xl">Rediger profil</DialogTitle>
                <label>
                  Her kan du endre profilen din. Trykk på "Lagre" når du er ferdig.
                </label>
                <Input
                  v-model="username"
                  placeholder="Navn"
                  type="text"
                />
                <Input
                  v-model="email"
                  placeholder="Epostadresse"
                  type="email"
                /><br/>
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
                  <Button class="w-48">Lagre</Button>
                </DialogClose>
              </DialogFooter>
            </DialogContent>
          </Dialog>
          <br/>
          <label>
            Husstand:
          </label>
          <CardDescription>
            <p>Husstandsnavn: {{ householdName }}</p>
            <p>Lokasjon: {{ location === null ? 'Ikke spesifisert' : location }}</p>
            <p>Husstandsnummer: {{ householdNumber }}</p>
            <br/>
            [Medlem 1]<br/>
            [Medlem 2]<br/>
          </CardDescription>
          <div class="flex flex-col items-center">
            <Dialog>
              <DialogTrigger>
                <Button class="w-48">Endre husstand</Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle class="text-2xl">Endre husstand</DialogTitle>
                  <label>
                    Her kan du endre husstanden din. Trykk på "Lagre" når du er ferdig.
                  </label>
                  <Input
                    v-model="householdName"
                    placeholder="Husstandsnavn"
                    type="text"
                  />
                  <Input
                    v-model="location"
                    placeholder="Lokasjon (valgfritt)"
                    type="text"
                  /><br/>
                  <label>
                    <p>Husstandsnummer: {{ householdNumber }}</p><br/>
                  </label>
                  <DialogTitle>Medlemmer</DialogTitle>
                  <div class="flex items-center gap-2 justify-between">
                    <label>[Medlem1]</label>
                    <AlertDialog>
                      <AlertDialogTrigger as-child>
                        <Button size="icon" variant="outline">
                          <user-minus/>
                        </Button>
                      </AlertDialogTrigger>
                      <AlertDialogContent>
                        <AlertDialogHeader>
                          <AlertDialogTitle>Er du sikker?</AlertDialogTitle>
                          <AlertDialogDescription>
                            Er du sikker på at du ønsker å fjerne medlemmet fra husstanden?
                          </AlertDialogDescription>
                        </AlertDialogHeader>
                        <AlertDialogFooter>
                          <AlertDialogCancel>Nei</AlertDialogCancel>
                          <AlertDialogAction>Ja</AlertDialogAction>
                        </AlertDialogFooter>
                      </AlertDialogContent>
                    </AlertDialog>
                  </div>
                </DialogHeader>

                <DialogFooter class="flex flex-col items-center">
                  <DialogClose>
                    <Button class="w-48">Lagre</Button>
                  </DialogClose>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </div>
        </div>
      </CardContent>
    </Card>
    <router-link to="/login">
      <Button class="w-48" variant="destructive">Logg ut</Button>
    </router-link>
  </div>
</template>

<style scoped>

</style>
