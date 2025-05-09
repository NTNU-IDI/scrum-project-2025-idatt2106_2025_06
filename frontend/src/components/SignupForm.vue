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
import { onMounted, ref } from 'vue'
import {useRouter} from "vue-router";
import axios from "axios";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from '@/components/ui/dialog';

const recaptchaSiteKey = import.meta.env.VITE_RECAPTCHA_SITE_KEY;
const name = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const recaptchaToken = ref('');

const errorMessage = ref("");
const isDialogOpen = ref(false);

const privacyAccepted = ref(false);
const showPrivacyDialog = ref(false);
const privacyText = ref('');

const router = useRouter();


const registerUser = async () => {
  if (password.value !== confirmPassword.value) {
    errorMessage.value = "Passordene må være like.";
    return;
  }

  if (!recaptchaToken.value) {
    errorMessage.value = "Vennligst fullfør reCAPTCHA.";
    return;
  }

  if (!privacyAccepted.value) {
    errorMessage.value = "Du må godta personvernerklæringen.";
    return;
  }

  try {
    const response = await axios.post("http://localhost:8080/api/auth/signup", {
      name: name.value,
      email: email.value,
      password: password.value,
      recaptcha: recaptchaToken.value,
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

const loadRecaptcha = () => {

  const script = document.createElement("script");
  script.src = "https://www.google.com/recaptcha/api.js";
  script.async = true;
  script.defer = true;
  document.body.appendChild(script);
}

onMounted(async () => {
  window.handleRecaptchaResponse = (token) => {
    recaptchaToken.value = token
  }

  loadRecaptcha();

  try {
    const response = await fetch('/personvern.txt')
    privacyText.value = await response.text()
  } catch (err) {
    console.error('Klarte ikke laste personvernerklæring:', err)
    privacyText.value = 'Kunne ikke laste personvernerklæring.'
  }
});
</script>

<template>
  <div class="m-auto">
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
            <div class="w-full overflow-x-auto">
              <div
                class="g-recaptcha"
                :data-sitekey="recaptchaSiteKey"
                data-callback="handleRecaptchaResponse"
              ></div>
            </div>

            <div class="flex items-start gap-2 mt-2">
              <input
                id="privacy"
                type="checkbox"
                v-model="privacyAccepted"
                class="accent-blue-600 mt-1"
              />
              <label for="privacy" class="text-sm">
                Jeg godtar
                <button
                  type="button"
                  class="underline text-blue-600 hover:text-blue-800"
                  @click="showPrivacyDialog = true"
                >
                  personvernerklæringen
                </button>.
              </label>
            </div>

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
  </div>
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

  <Dialog :open="showPrivacyDialog" @update:open="showPrivacyDialog = $event">
    <DialogContent class="max-w-lg">
      <DialogHeader>
        <DialogTitle>Personvernerklæring</DialogTitle>
        <DialogDescription>
          <div class="max-h-64 overflow-y-auto text-sm whitespace-pre-line mt-2">
            {{ privacyText }}
          </div>
        </DialogDescription>
      </DialogHeader>
    </DialogContent>
  </Dialog>
</template>
