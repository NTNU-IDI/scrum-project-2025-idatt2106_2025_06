<script setup>

import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card/index.js'
import { Button } from '@/components/ui/button/index.js'
import { Checkbox } from '@/components/ui/checkbox/index.js'
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible/index.js'
import { ChevronDown, ChevronRight } from 'lucide-vue-next'
import { computed, onMounted, ref, watch } from 'vue'
import { useSessionStore } from '@/stores/session.js'
import { useChecklistStore } from '@/stores/checklist.js'
import { DonutChart } from '@/components/ui/chart-donut/index.js'

const sessionStore = useSessionStore();
const checklistStore = useChecklistStore();

const checklistPoints = computed(() => checklistStore.allCheckpoints);
const selectedChecklistPoints = ref(new Set());

const startupFinished = ref(false);

const percentage = ref();

const data = computed(() => {
  if (percentage.value === undefined) {
    return [];
  } else {
    return [
      { name: 'Klar', total: Math.floor(percentage.value) },
      { name: 'Ikke klar', total: Math.floor(100 - percentage.value) },
    ];
  }
});

const percentageColors = computed(() => {
  if (percentage.value === 100) {
    return ['#34D399', '#e5e7eb']
  } else if (percentage.value >= 50) {
    return ['#facc15', '#e5e7eb']
  } else {
    return ['#ef4444', '#e5e7eb']
  }
})

const toggleItem = (id, value) => {
  if (value) {
    selectedChecklistPoints.value.add(id);
  } else {
    selectedChecklistPoints.value.delete(id);
  }
};

const handleUpdate = async () => {
  await checklistStore.updateChecklist(Array.from(selectedChecklistPoints.value));
}

watch(() => checklistStore.percentageCompleted, async() => {
  percentage.value = checklistStore.percentageCompleted;
})

onMounted(async () => {
  if (sessionStore.isAuthenticated) {
    await checklistStore.getChecklist();
    await checklistStore.getMySelectedChecklist();
    if (checklistStore.selectedCheckpoints.length > 0) {
      selectedChecklistPoints.value = new Set(checklistStore.selectedCheckpoints);
      percentage.value = checklistStore.percentageCompleted;
    }
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
      <!--<p class="text-base font-bold mb-4">
        Beredskapsprosent: {{ checklistStore.percentageCompleted ? Math.floor(checklistStore.percentageCompleted) : 0 }}%
      </p>-->
      <div class="justify-center mb-6">
        <DonutChart :category="'total'" index="name" :data="data" :colors="percentageColors" class="h-32"/>
      </div>
      <div v-for="item in checklistPoints" :key="item.id" class="flex flex-col w-full">
        <Collapsible v-slot="{ open }" class="w-full">
            <div class="flex items-center justify-start w-full text-left rounded hover:bg-gray-100">
              <Checkbox class="m-1.5" :model-value="selectedChecklistPoints.has(item.id)"
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
