<script setup>
import {Button} from "@/components/ui/button/index.js";
import { BentoCard, BentoGrid } from '@/components/ui/bento'
</script>

<template>
  <div class="m-auto">
    <BentoGrid>
      <!-- Card 1 -->
      <BentoCard
        name="Krisescenarioer"
        description="Les om forskjellige krisescenarioer"
        cta="Les mer"
        href="/krise"
        :Icon="ArrowRightIcon"
        customClass="col-span-2"
      >
        <template #background>
          <div class="absolute inset-0 bg-neutral-200" />
        </template>
      </BentoCard>

      <!-- Card 2 -->
      <BentoCard
        name="Krisehåndtering"
        description="Generell info om krisehåndtering (før/under/etter)"
        cta="Les mer"
        href="/info"
        :Icon="ArrowRightIcon"
        customClass="col-span-2"
      >
        <template #background>
          <div class="absolute inset-0 bg-neutral-200" />
        </template>
      </BentoCard>

      <!-- Nyheter -->
      <BentoCard
        name="Nyheter"
        cta="Gå til nyheter"
        href="/nyheter"
        :Icon="ArrowRightIcon"
        customClass="col-span-1 min-h-[20rem]"
      >
        <template #background>
          <div class="absolute inset-0 bg-neutral-200" />
        </template>
      </BentoCard>

      <!-- Utgår snart -->
      <BentoCard
        name="Utgår snart"
        description="Du er innlogget og vil se hva som snart går ut på dato"
        cta="Se detaljer"
        href="/utgar"
        :Icon="ArrowRightIcon"
        customClass="col-span-3 min-h-[24rem]"
      >
        <template #background>
          <div class="absolute inset-0 bg-neutral-200" />
        </template>
      </BentoCard>
    </BentoGrid>
  </div>

</template>

<style scoped></style>
