<script setup>
import { BentoCard, BentoCardCustom, BentoGrid } from '@/components/ui/bento'
import AlertCard from '@/components/AlertCard.vue'
</script>

<template>
  <div class="m-auto mt-20">
    <BentoGrid>
      <!-- Card 1 -->
      <BentoCard
        :Icon="ArrowRightIcon"
        cta="Les mer"
        customClass="col-span-2"
        description="Les om forskjellige krisescenarioer"
        href="/scenario"
        name="Krisescenarioer"
      />

      <!-- Card 2 -->
      <BentoCard
        :Icon="ArrowRightIcon"
        cta="Les mer"
        customClass="col-span-2"
        description="Generell info om krisehåndtering (før/under/etter)"
        href="/info"
        name="Krisehåndtering"
      >
      </BentoCard>

      <!-- Nyheter -->
      <BentoCardCustom
        customClass="col-span-1 min-h-[30rem] group hover:group-hover:bg-transparent"
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

      <!-- Utgår snart -->
      <!-- TODO legge til beredskapslager her -->
      <BentoCardCustom customClass="col-span-3 min-h-[30rem]" href="/inventory" name="Utgår snart">
        <div>TBA</div>
      </BentoCardCustom>
    </BentoGrid>
  </div>
</template>

<style scoped></style>
