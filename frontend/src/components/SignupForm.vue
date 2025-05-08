<script setup>
import {Button} from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import {Input} from '@/components/ui/input';
import {Label} from '@/components/ui/label';
import {ref} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from '@/components/ui/dialog';


const name = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");

const errorMessage = ref("");
const isDialogOpen = ref(false);

const router = useRouter();

const registerUser = async () => {
  if (password.value !== confirmPassword.value) {
    errorMessage.value = "Passordene må være like.";
    return;
  }


  try {
    const response = await axios.post("http://localhost:8080/api/auth/signup", {
      name: name.value,
      email: email.value,
      password: password.value,

    });

    if(response.data.verified) {
      router.push("/login");
    }
    else {
      isDialogOpen.value = true;
    }

  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message
    } else {
      errorMessage.value = "Registrering feilet. Prøv igjen senere."
    }

    console.error("Feil ved registrering:", error);
  }
};

const closeDialog = () => {
  isDialogOpen.value = false;
  router.push("/login");
};
</script>

<template>
  <Card class="mx-auto max-w-sm">
    <CardHeader>
      <CardTitle class="text-2xl"> Registrer ny bruker</CardTitle>
      <CardDescription>
        Skriv inn epost-adressen din for å logge inn
      </CardDescription>
    </CardHeader>
    <CardContent>
      <div class="grid gap-4">
        <form @submit.prevent="registerUser">
          <div class="grid gap-2">
            <Label for="name">Navn</Label>
            <Input id="name" v-model="name" placeholder="Ola Nordmann" required type="text"/>

            <Label for="email">Epost</Label>
            <Input id="email" v-model="email" placeholder="m@example.com" required type="email"/>

            <Label for="password">Passord</Label>
            <Input id="password" v-model="password" required type="password"/>

            <Label for="confirmpassword">Bekreft passord</Label>
            <Input id="confirmpassword" v-model="confirmPassword" required type="password"/>
            <Button class="w-full" type="submit">Registrer</Button>
            <p>
              <span v-if="errorMessage" class="text-red-500 font-bold">{{ errorMessage }}</span>
            </p>
          </div>
        </form>
      </div>
      <div class="mt-4 text-center text-sm">
        Har du bruker fra før?
        <router-link class="underline" to="/login"> Logg inn</router-link>
      </div>
    </CardContent>
  </Card>

  <Dialog :open="isDialogOpen" @update:open="isDialogOpen = $event">
    <DialogContent>
      <DialogHeader>
        <DialogTitle>E-postbekreftelse</DialogTitle>
        <DialogDescription>
          En bekreftelseslenke er sendt til e-posten din. Vennligst sjekk innboksen din for å bekrefte kontoen.
        </DialogDescription>
      </DialogHeader>
      <Button class="mt-4" @click="closeDialog">OK</Button>
    </DialogContent>
  </Dialog>
</template>
