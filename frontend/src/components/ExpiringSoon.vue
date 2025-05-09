<script setup>

import { Button } from '@/components/ui/button/index.js'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table/index.js'
import { useInventoryStore } from '@/stores/inventory.js'
import { computed, onMounted, ref, watch } from 'vue'
import { useStorageStore } from '@/stores/storage.js'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select/index.js'
import { DonutChart } from '@/components/ui/chart-donut/index.js'
import { useSessionStore } from '@/stores/session.js'
import { useChecklistStore } from '@/stores/checklist.js'

const storageStore = useStorageStore();
const inventoryStore = useInventoryStore();
const sessionStore = useSessionStore();
const checklistStore = useChecklistStore();

const items = computed(() => inventoryStore.expiresSoonItems)
const storages = computed(() => storageStore.storages)
const activeStorageId = ref();
const activeStorageName = ref();

const userLoggedIn = computed(() => sessionStore.token);

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

const getAmountAndUnit = (amount, unitId) => {
  if (unitId === 1) {
    return amount + " stk";
  } else if (unitId === 2) {
    if (amount > 999) {
      return amount / 1000 + " kg";
    } else {
      return amount + " g";
    }
  } else if (unitId === 3) {
    if (amount < 1) {
      return amount * 10 + " dl";
    } else {
      return amount + " liter";
    }
  }
}

const getDaysLeft = (expDate) => {
  const expirationDate = new Date(expDate);
  expirationDate.setHours(0, 0, 0, 0);

  const currentDate = new Date();
  currentDate.setHours(0, 0, 0, 0);

  const dayDiff = expirationDate - currentDate;
  const msToDays = 1000 * 60 * 60 * 24;

  const dayAmount = dayDiff / msToDays;
  if (dayAmount >= 365) {
    const years = Math.floor(dayAmount / 365);
    return years + " år";
  } else if (dayAmount >= 30) {
    const months = Math.floor(dayAmount / 30);
    if (months === 12) {
      return "1 år";
    } else if (months === 1) {
      return months + " måned";
    } else {
      return months + " måneder";
    }
  } else if (dayAmount >= 7) {
    const weeks = Math.floor(dayAmount / 7);
    if (weeks === 1) {
      return weeks + " uke";
    } else {
      return weeks + " uker";
    }
  } else if (dayAmount === 0) {
    return "I dag"
  } else if (dayAmount < 0) {
    return "Utgått"
  } else {
    if (dayAmount === 1) {
      return dayAmount + " dag";
    } else {
      return dayAmount + " dager";
    }
  }
}

const getExpirationColor = (expDate) => {
  const expirationDate = new Date(expDate);
  expirationDate.setHours(0, 0, 0, 0);

  const currentDate = new Date();
  currentDate.setHours(0, 0, 0, 0);

  const dayDiff = expirationDate - currentDate;
  const msToDays = 1000 * 60 * 60 * 24;

  const dayAmount = dayDiff / msToDays;
  if (dayAmount < 7) {
    return 'text-red-600 font-medium';
  }
}

const fetchReadiness = async () => {
  await inventoryStore.getExpiresSoonItems(activeStorageId.value);
}

watch(() => activeStorageId.value, async (newId) => {
  const newSelection = storages.value.find(storage => storage.id === newId);
  activeStorageName.value = newSelection.name;

  await fetchReadiness();
})

const startupFinished = ref(false);


onMounted(async () => {
  if (sessionStore.isAuthenticated) {
    try {
      await storageStore.fetchAll(sessionStore.token)

      activeStorageId.value = storages.value[0].id;
      activeStorageName.value = storages.value[0].name;
    } catch (error) {
      console.error("Klarte ikke hente husstander:", error)
    }
    await inventoryStore.getExpiresSoonItems(activeStorageId.value);
    await checklistStore.getMySelectedChecklist();
    percentage.value = checklistStore.percentageCompleted;
  }

  startupFinished.value = true;
});
</script>

<template>
  <div v-if="!userLoggedIn" class="flex flex-col min-h-[20em] gap-3 justify-center">
    <p class="text-lg text-center font-bold">Du må være innlogget for å se beredskapsgrad og beredskapslager.</p>
    <RouterLink to="/login" class="mx-auto">
      <Button>Logg inn</Button>
    </RouterLink>
  </div>
  <div v-else-if="startupFinished && storages.length === 0" class="flex flex-col min-h-[20em] gap-3 justify-center">
    <p class="text-lg font-bold text-center"> Finner ingen husstander </p>
    <p class="text-center">For å se lager må du være med i en husstand eller opprett en egen.</p>
    <RouterLink to="/profile" class="mx-auto">
      <Button>Bli med i husstand</Button>
    </RouterLink>
  </div>
  <div v-else-if="startupFinished && storages.length > 0" class="flex flex-col md:flex-row items-center md:items-start w-full min-h-full">
    <div class="grid w-full grid-cols-2 gap-3">
      <p class="font-medium text-base col-span-2">Beredskapsgrad</p>
      <div class="col-span-1 md:col-span-2 xl:mt-6">
        <DonutChart :category="'total'" index="name" :data="data" :colors="percentageColors" />
      </div>
      <div class="col-span-1 md:col-span-2  md:mx-auto">
        <div class="grid w-full grid-cols-1 gap-3 mx-auto justify-items-center">
          <p class="font-medium text-sm mx-auto col-span-1">Bytt husstand</p>
          <Select v-if="activeStorageId" v-model="activeStorageId" class="col-span-1">
            <SelectTrigger>
              <SelectValue placeholder="Velg sted" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem v-for="storage in storages" :key="storage.id" :value="storage.id">
                  {{ storage.name }}
                </SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
          <RouterLink to="/inventory" class="col-span-1 w-full">
            <Button class="whitespace-normal break-words text-center min-h-[3rem] md:min-h-[auto]">Gå til sjekkliste og lager</Button>
          </RouterLink>
        </div>
      </div>
    </div>

    <div class="h-px w-full md:w-px md:min-h-[25rem] md:h-auto bg-gray-300 m-[16px] md:my-[auto]"></div>

    <div class="w-full max-h-[30rem] flex flex-col overflow-y-auto gap-2">
      <p class="font-medium text-base">Utgår neste 3 månedene</p>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead class="w-[40%]">Vare</TableHead>
            <TableHead class="text-center w-[30%]">Mengde</TableHead>
            <TableHead class="text-center w-[30%]">Utløpsdato</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="item in items" :key="item.id">
            <TableCell class="font-medium w-[40%]">{{ item.name }}</TableCell>
            <TableCell class="text-center w-[30%]">{{ getAmountAndUnit(item.amount, item.unitId) }}</TableCell>
            <TableCell class="text-center w-[30%]" :class="getExpirationColor(item.expiryDate)">
              {{ getDaysLeft(item.expiryDate) }}
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </div>
  </div>
</template>

<style scoped>

</style>
