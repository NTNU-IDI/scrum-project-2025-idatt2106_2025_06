<script setup>
import {Alert} from '@/components/ui/alert/index.js'
import { BentoCard, BentoCardCustom, BentoGrid } from '@/components/ui/bento'
</script>

<template>
  <div class="m-auto mt-20">
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
      </BentoCard>

      <!-- Nyheter -->
      <BentoCardCustom
        name="Nyheter"
        customClass="col-span-1 min-h-[30rem] group hover:group-hover:bg-transparent"
      >
        <div class="h-[25rem] overflow-y-auto space-y-2 p-2">
          <Alert variant="default">
            <AlertTitle>Info: </AlertTitle>
            <AlertDescription>En bombe er sluppet på sluppen, alle eksamener avlyst.</AlertDescription>
          </Alert>
          <Alert variant="default">
            <AlertTitle>Info: </AlertTitle>
            <AlertDescription>Gratis bolle på Element.</AlertDescription>
          </Alert>
          <Alert variant="default">
            <AlertTitle>Info: </AlertTitle>
            <AlertDescription>Snart skal alerts slutte å vises. Dette skal kunne scrolles plis</AlertDescription>
          </Alert>
          <Alert variant="default">
            <AlertTitle>Info: </AlertTitle>
            <AlertDescription>Håper denne er borte.</AlertDescription>
          </Alert>
          <Alert variant="default">
            <AlertTitle>Info: </AlertTitle>
            <AlertDescription>Borte... bø!.</AlertDescription>
          </Alert>
        </div>
      </BentoCardCustom>


      <!-- Utgår snart -->
      <BentoCardCustom
        name="Utgår snart"
        customClass="col-span-3 min-h-[30rem]"
      >
        <div>
          TBA
        </div>
      </BentoCardCustom>
    </BentoGrid>
  </div>
</template>

<style scoped></style>
