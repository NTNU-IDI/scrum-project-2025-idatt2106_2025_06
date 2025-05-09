<script setup>

import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card/index.js'
import { Button } from '@/components/ui/button/index.js'
import { Checkbox } from '@/components/ui/checkbox/index.js'
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible/index.js'
import { ChevronDown, ChevronRight } from 'lucide-vue-next'
import { computed, onMounted, ref } from 'vue'
import { useSessionStore } from '@/stores/session.js'
import { useChecklistStore } from '@/stores/checklist.js'
import { updateCheckpoints } from '@/service/checklistService.js'

const sessionStore = useSessionStore();
const checklistStore = useChecklistStore();

//const checklistPoints = computed(() => checklistStore.allCheckpoints);
//const selectedChecklistPoints = ref(new Set());
const selectedChecklistPoints = ref(new Set());

const checkpointList = [
  { id: 'a9120ad81', name: 'Drikkevann', description: 'Rent drikkevann lagret på dunker eller flasker.' },
  { id: 'dde4ace9', name: 'Mat', description: 'Mat som tåler å bli lagret i romtemperatur.' },
  { id: '9d3c66d3', name: 'Grill, kokeapparat eller stormkjøkken', description: 'Grill, kokeapparat eller stormkjøkken.' },
  { id: '3457f6fd', name: 'Gassbeholder eller brennstoff', description: 'Ekstra gassbeholder eller brennstoff.' },
  { id: 'de2aeb6a', name: 'Kontanter', description: 'Litt kontanter og flere betalingskort.' },
  { id: '34a88ec2', name: 'Varme klær', description: 'Varme klær og pledd, dyner eller soveposer.' },
  { id: '2b2500e5', name: 'Fyrstikker og stearinlys', description: 'Fyrstikker og stearinlys.' },
  { id: '769efb16', name: 'Ved', description: 'Ved - Dersom du har vedovn eller peis.' },
  { id: '4ca2ff28', name: 'Gass- eller parafinovn', description: 'Gass- eller parafinovn som er beregnet til innendørs bruk.' },
  { id: '97220168', name: 'Lomme- eller hodelykt', description: 'Lommelykter eller hodelykter som går på batteri, sveiv eller solceller.' },
  { id: '54b56812', name: 'DAB-radio', description: 'DAB-radio som går på batteri, sveiv eller solceller.' },
  { id: 'b8f8cfc7', name: 'Batteri og mobilbank', description: 'Batteri og oppladet batteribank.' },
  { id: 'ef9dc91d', name: 'Liste med viktige telfonnummere', description: 'Liste på papir med viktige telefonnummer og nummeret til legevakt, vetrinær, familie, venner og naboer.' },
  { id: 'e9d304cf', name: 'Legemiddel og førstehjelpsutstyr', description: 'Legemiddel og førstehjelpsutstyr' },
  { id: '69116743', name: 'Jodtabletter', description: 'Jodtabletter(gjelder barn og voksne under 40 år, gravide og ammende)' },
  { id: '41e492ff', name: 'Hygiene artikler', description: 'Hygieneartikler i form av våtservietter, håndsprit, bleier, toalettpapir, bind og tamponger.' }
];

const startupFinished = ref(false);

const toggleItem = (id, value) => {
  if (value) {
    selectedChecklistPoints.value.add(id);
  } else {
    selectedChecklistPoints.value.delete(id);
  }
  console.log(selectedChecklistPoints.value);
};

const handleUpdate = () => {

}

onMounted(async () => {
  if (sessionStore.isAuthenticated) {
    //await checkpointList.getChecklist();
    //await checkpointList.getMySelectedChecklist();
    //selectedChecklistPoints.value = checklistStore.selectedCheckpoints;
  }

  startupFinished.value = true;
});
</script>

<template>
  <Card class="w-[350px] my-5" v-if="startupFinished">
    <CardHeader>
      <CardTitle class="text-xl">Beredskapsgrad</CardTitle>
      <CardDescription>
        Sjekk av punktene manuelt for å se hvor forberedt du er. Må være nok for en uke uten hjelp
        fra myndigheter. Anbefaler ca 20L vann for en uke per pers. Trykk på punktet for mer detaljer.
      </CardDescription>
    </CardHeader>
    <CardContent>
      <div v-for="item in checkpointList" :key="item.id" class="flex-col w-full">
        <Collapsible v-slot="{ open }" class="w-full">
            <div class="flex items-center justify-start w-full text-left rounded hover:bg-gray-100">
              <!--<Checkbox class="m-1.5" :model-value="selectedChecklistPoints.has(item.id)"
                        @update:modelValue="value => toggleItem(item.id, value)"/>-->
              <Checkbox class="m-1.5"
                        @update:modelValue="value => toggleItem(item.id, value)"/>
              <CollapsibleTrigger class="w-full flex justify-between items-center">
                <p class="flex font-semibold leading-none tracking-tight justify-start w-full">
                  {{ item.name }}
                </p>
                <ChevronRight v-if="!open" class="text-gray-400"/>
                <ChevronDown v-if="open" class="text-gray-400"/>
              </CollapsibleTrigger>
            </div>
          <CollapsibleContent>
            <p class="ml-7 mb-2">
              {{ item.description }}
            </p>
          </CollapsibleContent>
        </Collapsible>
      </div>

    </CardContent>
    <CardFooter>
      <Button @click="handleUpdate">Oppdater</Button>
    </CardFooter>
  </Card>
</template>

<style scoped>

</style>
