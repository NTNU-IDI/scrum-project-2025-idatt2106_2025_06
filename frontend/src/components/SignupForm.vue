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
        <div class="grid gap-2">
          <Label for="email">Epost</Label>
          <Input id="email" placeholder="me@example.com" required type="email"/>
        </div>
        <div class="grid gap-2">
          <Label for="email">Navn</Label>
          <Input id="email" placeholder="Ola Nordmann" required type="email"/>
        </div>
        <div class="grid gap-2">
          <Label for="password">Passord</Label>
          <Input id="password" required type="password"/>
        </div>
        <div class="grid gap-2">
          <Label for="password">Bekreft passord</Label>
          <Input id="password" required type="password"/>
        </div>
        <Button class="w-full" type="submit"> Registrer</Button>
      </div>
      <div class="mt-4 text-center text-sm">
        Har du bruker fra før?
        <router-link class="underline" to="/login"> Logg inn</router-link>
      </div>
    </CardContent>
  </Card>
</template>
