<script setup>
import { BentoCard, BentoCardCustom, BentoGrid } from '@/components/ui/bento'
import AlertCard from '@/components/AlertCard.vue'
import ExpiringSoon from '@/components/ExpiringSoon.vue'
import { ArrowRightIcon } from 'lucide-vue-next'
</script>

<template>
  <div class="m-3 sm:md-auto md:mx-4 lg:my-4 lg:mx-auto flex justify-center min-h-screen">
    <BentoGrid class="my-auto">
      <BentoCard
        :Icon="ArrowRightIcon"
        cta="Les mer"
        customClass="col-span-1 sm:col-span-1 sm:row-start-1
        md:col-span-2 md:row-start-1
        lg:col-span-2 lg:row-start-1"
        description="Les om forskjellige krisescenarioer"
        href="/scenario"
        name="Krisescenarioer"
      />

      <BentoCard
        :Icon="ArrowRightIcon"
        cta="Les mer"
        customClass="col-span-1 sm:col-span-1 sm:row-start-2
        md:col-span-2 md:row-start-2
        lg:col-span-2 lg:row-start-1"
        description="Generell info om krisehåndtering (før/under/etter)"
        href="/info"
        name="Krisehåndtering"
      >
      </BentoCard>

      <BentoCardCustom
        customClass="col-span-1 sm:col-span-1 sm:row-span-2 sm:col-start-2
        md:col-span-1 md:row-span-2 md:col-start-3
        lg:col-span-1 lg:row-span-1 lg:col-start-1
        min-h-[30rem] group hover:group-hover:bg-transparent"
        href="/alerts"
        name="Nyheter"
      >
        <div class="h-[25rem] overflow-y-auto flex flex-col gap-2">
          <!-- TODO: Implementere dynamisk generering av alerts fra databasen -->
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
        </div>
      </BentoCardCustom>

      <BentoCardCustom customClass="col-span-1 sm:col-span-2 md:col-span-3 lg:col-span-3 min-h-[30rem] lg:w-[45rem]" class="cursor-default" name="Beredskap">
        <ExpiringSoon/>
      </BentoCardCustom>
    </BentoGrid>
  </div>
</template>

<style scoped></style>
