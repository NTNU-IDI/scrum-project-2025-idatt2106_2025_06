<script setup>
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
</script>

<template>
  <div class="m-auto mt-10">
    <Card class="mx-auto w-full items-center flex flex-col max-w-2xl p-6 shadow-md rounded-2xl">
      <CardHeader>
        <CardTitle class="text-xl font-bold">
          Etter en krise
        </CardTitle>
      </CardHeader>
      <CardContent>
        <p class="text-base mt-2">
          Det er vanlig å få ulike reaksjoner etter en krise eller katastrofe.
          Hvis du sliter etter å ha opplevd en traumatisk hendelse, bør du vurdere å søke hjelp.
          Det er viktig å ta vare på deg selv og dine nærmeste.
          Når krisen er over, er det tid for å gjenopprette normalitet – både fysisk og mentalt.
          Det kan ta tid, og det er helt normalt å føle på usikkerhet eller etterreaksjoner. <br />

          Her er noen råd for tiden etter: <br />

          - Følg fortsatt med på informasjon fra myndighetene – det kan komme oppdaterte råd og støtteordninger.<br />
          - Sjekk boligen din for skader, og ta kontakt med forsikring eller fagpersoner ved behov.<br />
          - Snakk med noen om det du har opplevd. Det hjelper – både for barn og voksne.<br />
          - Evaluer hva som fungerte godt og hva som kunne vært bedre i din beredskap. Oppdater nødlager og plan etter erfaringene.<br />
          - Ta deg tid til å hente deg inn. Gjenopprett rutiner og ta vare på helsa.<br />
          <br />
          Krisen er over – men forberedelse til neste gang begynner nå. Du er allerede bedre rustet enn før.
          <br />

        </p>
      </CardContent>
    </Card>

    <div class="mt-12 flex flex-wrap justify-center gap-6">
      <router-link to="/info">
        <Button class="px-10 py-6">
          Tilbake
        </Button>
      </router-link>
      <router-link to="/before">
        <Button class="px-10 py-6">
          Før
        </Button>
      </router-link>
    </div>
  </div>

</template>


