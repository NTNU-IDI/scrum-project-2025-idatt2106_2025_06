<script setup>
import { BentoCardCustom } from '@/components/ui/bento/index.js'
import { Alert } from '@/components/ui/alert/index.js'
import BentoGrid from '@/components/ui/bento/BentoGrid.vue'
</script>

<template>
  <!-- TODO Legge til kart som kan kobles til relaterte alerts?-->
  <!-- TODO Legge til lucide-vue-next så vi kan ha enkle etiketter til varslingene-->
  <div class="m-auto mt-20">
    <bento-grid>
    <BentoCardCustom
      name='Varslinger'
      customClass="col-span-4 min-h-[40rem] group hover:group-hover:bg-transparent"
    >
      <div class="h-[35rem] overflow-y-auto space-y-2 p-2">
        <!-- TODO Alle hardkoda alerts skal så klart fjernes etter hvert-->
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>En bombe er sluppet på sluppen, alle eksamener avlyst.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Gratis bolle på Element.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Snart skal alerts slutte å vises. Dette skal kunne scrolles plis</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Håper denne er borte.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Borte... bø!.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>En bombe er sluppet på sluppen, alle eksamener avlyst.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Gratis bolle på Element.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Snart skal alerts slutte å vises. Dette skal kunne scrolles plis</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Håper denne er borte.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Borte... bø!.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>En bombe er sluppet på sluppen, alle eksamener avlyst.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Gratis bolle på Element.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Snart skal alerts slutte å vises. Dette skal kunne scrolles plis</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Håper denne er borte.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Borte... bø!.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>En bombe er sluppet på sluppen, alle eksamener avlyst.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Gratis bolle på Element.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Snart skal alerts slutte å vises. Dette skal kunne scrolles plis</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Håper denne er borte.</AlertDescription></Alert>
        <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Borte... bø!.</AlertDescription></Alert>
      </div>
    </BentoCardCustom>
    </bento-grid>
  </div>
</template>
<style scoped></style>
