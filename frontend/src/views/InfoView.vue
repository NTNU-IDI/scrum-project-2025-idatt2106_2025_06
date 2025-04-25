<script setup>
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import BentoCard from "@/components/ui/bento/BentoCard.vue";
</script>

<template>
  <div class="m-auto mt-10">
      <Card class="mx-auto w-full items-center flex flex-col max-w-2xl p-6 shadow-md rounded-2xl">
        <CardHeader>
          <CardTitle class="text-xl font-bold">
            Med krisefikser kan du styrke din egenberedskap!
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p class="text-base mt-2">
            Det er viktig å være beredt i tilfelle en krise skulle oppstå. bladi bla.. More info senere. <br />
            Nedenfor finner du informasjon om hva du bør gjøre før, under, og etter en krise.
          </p>
        </CardContent>
      </Card>

    <div class="w-full max-w-2xl mx-auto mt-10 px-4">
      <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <BentoCard
          name="Før"
          description="Beredskap før en krise inntreffer"
          cta="Les mer"
          href="/before"
          bgColor="bg-blue-100"
        />
        <BentoCard
          name="Under"
          description="Beredskap når en krise har inntruffet"
          cta="Les mer"
          href="/during"
          bgColor="bg-blue-200"
        />

        <BentoCard
          name="Etter"
          description="Beredskap etter en krise inntreffer"
          cta="Les mer"
          href="/after"
          bgColor="bg-blue-300"
        />
      </div>
    </div>
  </div>


</template>
