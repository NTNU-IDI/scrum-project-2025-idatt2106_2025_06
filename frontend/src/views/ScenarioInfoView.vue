<script setup>

import { BentoCard, BentoGrid } from '@/components/ui/bento/index.js'

</script>

<template>
  <div class="m-auto">
  <BentoGrid>
    <BentoCard
      name="Brann"
      description="Lær hvordan du forbereder deg på og håndterer brann i hjemmet eller nærområdet."
      href="/scenario"
      cta="Les mer"
      :Icon="ArrowRightIcon"
      customClass="col-span-2"
    />

    <BentoCard
      name="Flom"
      description="Få oversikt over hva du bør gjøre før, under og etter en flomsituasjon for å holde deg trygg."
      href="/info"
      cta="Les mer"
      :Icon="ArrowRightIcon"
      customClass="col-span-2"
    />

    <BentoCard
      name="Pandemi"
      description="Informasjon om hygiene, isolasjon og tiltak du bør følge under en smittsom sykdomsutbrudd."
      href="/info"
      cta="Les mer"
      :Icon="ArrowRightIcon"
      customClass="col-span-2"
    />

    <BentoCard
      name="Krig"
      description="Forstå hvordan du kan forberede deg mentalt og praktisk på uro, trusler og væpnet konflikt."
      href="/info"
      cta="Les mer"
      :Icon="ArrowRightIcon"
      customClass="col-span-2"
    />

    <BentoCard
      name="Strømbrudd"
      description="Slik klarer du deg uten strøm i korte og lengre perioder, og hva du bør ha tilgjengelig."
      href="/info"
      cta="Les mer"
      :Icon="ArrowRightIcon"
      customClass="col-span-2"
    />

    <BentoCard
      name="Matmangel"
      description="Lær hvordan du kan sikre matforsyning og ernæring i situasjoner med knapphet på mat."
      href="/info"
      cta="Les mer"
      :Icon="ArrowRightIcon"
      customClass="col-span-2"
    />
  </BentoGrid>
  </div>
</template>

<style scoped>

</style>
