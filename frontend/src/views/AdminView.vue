<script setup>
import { Button } from '@/components/ui/button/index.js'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card/index.js'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert/index.js'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs/index.js'

import { ref } from 'vue'
import AddModerator from '@/components/AddModerator.vue'
import DeleteModerator from '@/components/DeleteModerator.vue'
import ResetPasswordLink from '@/components/ResetPasswordLink.vue'

const isAdmin = ref(true) // endre til false for moderator
</script>

<template>
  <div class="m-auto flex flex-wrap relative w-full py-10 gap-6 items-stretch mt-10">
    <!-- Vis dette kortet KUN hvis man er admin -->
    <Card v-if="isAdmin" class="flex-1 basis-1/4 min-w-[200px] gap-6 max-h-[650px]">
      <CardHeader>
        <CardTitle class="text-2xl">Rediger moderatorer</CardTitle>
      </CardHeader>
      <p></p>
      <CardContent class="flex flex-col gap-2 h-full max-h-[calc(100%-80px)]">
      <AddModerator />
        <DeleteModerator />
        <ResetPasswordLink />
        <div class="grow"></div> <!-- Dette er helt forferdelig hardkoda, men la gå for nå -->
        <Button class="w-full" variant="destructive">Logg ut</Button>
      </CardContent>
    </Card>

    <!-- De to Card under vises alltid -->

    <!-- Redigere innlegg og varslinger -->
    <Card :class="isAdmin ? 'max-h-[650px] flex-1 basis-1/4 min-w-[200px]' : 'max-h-[650px] flex-1 basis-2/5 min-w-[300px]'">
      <Tabs default-value="post" class="">
        <TabsList class="grid w-full grid-cols-2">
          <TabsTrigger value="post">
            Innlegg
          </TabsTrigger>
          <TabsTrigger value="alert">
            Varsling
          </TabsTrigger>
        </TabsList>

        <!-- Varslinger-tab -->
        <TabsContent value="post">
          <div class="flex flex-col gap-2 p-5">
          <Button >Legg til nytt innlegg</Button>
            <CardContent class="flex flex-col overflow-y-auto max-h-[500px] gap-2 p-0">
              <!-- TODO Her hentes det egentlig fra databasen, dette er bare for å vise utseende og at man kan scrolle -->
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>En bombe er sluppet på sluppen, alle eksamener avlyst.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Gratis bolle på Element.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Systemet er under vedlikehold. Vær tålmodig.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Brannøvelse pågår. Ikke vær redd om alarmen går.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Vær forsiktig på vei hjem. Snø og glatte veier forventes.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Husk å sende inn oppgaven innen fredag kl. 23:59.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Viktige endringer i åpningstidene. Se nettsiden for detaljer.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Høy temperatur i kontorlokalene. Vennligst ta nødvendige forholdsregler.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Det er mulig å hente billetter til neste event i resepsjonen fra kl. 10:00.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Info: </AlertTitle><AlertDescription>Feil på serveren. Vi jobber med å løse problemet så raskt som mulig.</AlertDescription></Alert>
            </CardContent>
          </div>
        </TabsContent>

        <!-- Innlegg-tab -->
        <TabsContent value="alert">
          <div class="flex flex-col gap-2 p-5">
            <Button >Legg til ny varsling</Button>
            <CardContent class="flex flex-col overflow-y-auto max-h-[500px] gap-2 p-0">
              <!-- TODO Her hentes det egentlig fra databasen, dette er bare for å vise utseende og at man kan scrolle -->
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>En bombe er sluppet på sluppen, alle eksamener avlyst.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>Gratis bolle på Element.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>Systemet er under vedlikehold. Vær tålmodig.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>Brannøvelse pågår. Ikke vær redd om alarmen går.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>Vær forsiktig på vei hjem. Snø og glatte veier forventes.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>Husk å sende inn oppgaven innen fredag kl. 23:59.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>Viktige endringer i åpningstidene. Se nettsiden for detaljer.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>Høy temperatur i kontorlokalene. Vennligst ta nødvendige forholdsregler.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>Det er mulig å hente billetter til neste event i resepsjonen fra kl. 10:00.</AlertDescription></Alert>
              <Alert variant="default"><AlertTitle>Varsel: </AlertTitle><AlertDescription>Feil på serveren. Vi jobber med å løse problemet så raskt som mulig.</AlertDescription></Alert>
            </CardContent>
          </div>
        </TabsContent>
      </Tabs>

    </Card>

    <!-- Redigere kart -->
    <Card :class="isAdmin ? 'max-h-[650px] flex-1 basis-1/4 min-w-[200px]' : 'max-h-[650px] flex-1 basis-2/5 min-w-[300px]'">
      <CardHeader class="grid grid-cols-2 items-center w-full">
        <CardTitle class="text-2xl">Kart og hendelser</CardTitle>
        <router-link class="w-auto justify-self-end" to="/admin/map">
        <Button >Rediger</Button>
        </router-link>
      </CardHeader>
      <CardContent class="h-[80%]">
        <!-- TODO Inne her skal det være hendelser! -->
        <div class="bg-blue-300 flex h-[40%] m-2">

        </div>

        <!-- TODO Inne her skal det være kart! -->
        <div class="bg-blue-200 flex h-[60%] m-2">

        </div>
      </CardContent>
    </Card>
  </div>
</template>

<style scoped>

</style>
