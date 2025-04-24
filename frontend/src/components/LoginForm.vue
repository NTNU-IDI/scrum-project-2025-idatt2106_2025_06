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
      <CardTitle class="text-2xl"> Logg inn</CardTitle>
      <CardDescription>
        Skriv inn epost-adressen din for å logge inn
      </CardDescription>
    </CardHeader>
    <CardContent>
      <div class="grid gap-4">
        <div class="grid gap-2">
          <Label for="email">Epost</Label>
          <Input id="email" placeholder="m@example.com" required type="email"/>
        </div>
        <div class="grid gap-2">
          <div class="flex items-center">
            <Label for="password">Passord</Label>
            <a class="ml-auto inline-block text-sm underline" href="#">
              Glemt passord?
            </a>
          </div>
          <Input id="password" required type="password"/>
        </div>
        <Button class="w-full" type="submit"> Log inn</Button>
      </div>
      <div class="mt-4 text-center text-sm">
        Har du ikke bruker?
        <router-link class="underline" to="/signup"> Registrer her</router-link>
      </div>
    </CardContent>
  </Card>
</template>
