<script setup>
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import BentoCard from "@/components/ui/bento/BentoCard.vue";
</script>

<template>
  <div class="m-4 mt-10 xl:mx-auto">
      <Card class="mx-auto w-full items-center flex flex-col max-w-2xl p-6 shadow-md rounded-2xl">
        <CardHeader>
          <CardTitle class="text-xl font-bold">
            Med krisefikser kan du styrke din egenberedskap!
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p class="text-base mt-2">
            Norske myndigheter anbefaler at flest mulig er forberedt på å klare seg selv i en uke.
            Det er fordi kommuner og nødetater må prioritere de som ikke klarer seg uten hjelp i en krise. <br />
            God egenberedskap er en investering i trygghet for deg og dine nærmeste.I tillegg bidrar du til at myndighetene kan prioritere dem som trenger mest hjelp.<br />

            Ved å være forberedt i en uke blir du bedre i stand til å håndtere alt fra små forstyrrelser i hverdagen til større kriser.
            I tillegg har du bedre tid til å planlegge veien videre om krisen skulle bli langvarig. <br />

            Husk at noen forberedelser er mye bedre enn ingenting!
            Nedenfor finner du informasjon om hva du bør gjøre før, under, og etter en krise.
          </p>
        </CardContent>
      </Card>

    <div class="w-full max-w-2xl mx-auto mt-10 px-1">
      <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <BentoCard
          name="Før"
          description="Beredskap før en krise inntreffer"
          cta="Les mer"
          href="/before"
        />
        <BentoCard
          name="Under"
          description="Beredskap når en krise har inntruffet"
          cta="Les mer"
          href="/during"
        />

        <BentoCard
          name="Etter"
          description="Beredskap etter en krise inntreffer"
          cta="Les mer"
          href="/after"
        />
      </div>
    </div>
  </div>


</template>
