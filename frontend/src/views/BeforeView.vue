<script setup>
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
</script>

<template>
  <div class="m-auto mt-10">
    <Card class="mx-auto w-full items-center flex flex-col max-w-2xl p-6 shadow-md rounded-2xl">
      <CardHeader>
        <CardTitle class="text-xl font-bold">
          Beredskap før en krise inntreffer
        </CardTitle>
      </CardHeader>
      <CardContent>
        <p class="text-base mt-2">
          Mai 2024 endret Direktoratet for samfunnssikkerhet og beredskap råd for egenberedskap fra å gjelde i 72 timer (tre døgn) til 1 uke.

          Du må kunne ta vare på deg selv og dine nærmeste i 1 uke, i tilfelle strømmen går, vannet forsvinner, mobildekningen og internett faller ut, eller en annen slags krise inntreffer. Det er dette som kalles egenberedskap. Dersom flest mulig sørger for en god egenberedskap og har et lite lager hjemme, skaper vi et mer robust samfunn.

          Kommunen og nødetater må prioritere de som ikke klarer seg uten hjelp i en krise.

          Ha et lager hjemme
          Hvis strømmen blir borte eller du blir isolert av været eller andre forhold, må du kunne ta vare på deg selv i noen dager. For å dekke grunnleggende behov i minst tre dager trenger du varme, vann, mat, nødvendige legemidler, sanitærartikler og mulighet for å holde deg orientert om hva som skjer.
        </p>
      </CardContent>
    </Card>

    <div class="mt-12 flex flex-wrap justify-center gap-6">
      <Button class="px-10 py-6 bg-blue-300 hover:bg-blue-400">
        <router-link to="/info">Tilbake</router-link>
      </Button>
      <Button class="px-10 py-6 bg-blue-700 hover:bg-blue-400">
        <router-link to="/during">Under</router-link>
      </Button>
  </div>
  </div>

</template>


