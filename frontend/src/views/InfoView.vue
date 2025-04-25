<script setup>
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import BentoGrid from "@/components/ui/bento/BentoGrid.vue";
import BentoCard from "@/components/ui/bento/BentoCard.vue";
import {ArrowRightIcon} from "lucide-react";
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
        <RouterLink to="/for" class="block">
        <BentoCard class="bg-blue-400"
          name="Før"
          description="Beredskap før en krise inntreffer"
          cta="Les mer"
        />
        </RouterLink>
        <RouterLink to="/under" class="block">
        <BentoCard
          name="Under"
          description="Beredskap når en krise har inntruffet"
          cta="Les mer"
          href="/under"
        />
        </RouterLink>

        <RouterLink to="/etter" class="block">
        <BentoCard
          name="Etter"
          description="Beredskap etter en krise inntreffer"
          cta="Les mer"
          to="/etter"
        />
        </RouterLink>
      </div>
    </div>
        <!-- <Button class="px-10 py-6 bg-blue-700 hover:bg-blue-400">
          <router-link to="/for">Før</router-link>
        </Button>
        <Button class="px-10 py-6 bg-blue-700 hover:bg-blue-400">
          <router-link to="/under">Under</router-link>
        </Button>
        <Button class="px-10 py-6 bg-blue-700 hover:bg-blue-400">
          <router-link to="/etter">Etter</router-link>
        </Button> -->
      </div>


</template>
