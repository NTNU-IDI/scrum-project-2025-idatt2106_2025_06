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

const storageStore = useStorageStore();
const inventoryStore = useInventoryStore();
const sessionStore = useSessionStore();

const items = computed(() => inventoryStore.expiresSoonItems)
const storages = computed(() => storageStore.storages)
const activeStorageId = ref();
const activeStorageName = ref();

const percentage = 74;

const data = [
  { name: 'Klar', total: Math.floor(percentage) },
  { name: 'Ikke klar', total: Math.floor(100-percentage)},
];

const percentageColors = computed(() => {
  if (percentage === 100) {
    return ['#34D399', '#e5e7eb']
  } else if (percentage >= 50) {
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

onMounted(async () => {
  if (sessionStore.isAuthenticated) {
    activeStorageId.value = storages.value[0].id;
    activeStorageName.value = storages.value[0].name;

    await inventoryStore.getExpiresSoonItems(activeStorageId.value);
  }

});
</script>

<template>
  <div class="flex items-center w-full h-[25rem]">
    <div class="w-1/3 h-full flex flex-col justify-between">
      <p class="font-medium text-base">Beredskapsgrad</p>
      <div>
        <DonutChart :category="'total'" index="name" :data="data" :colors="percentageColors" />
      </div>
      <div class="mx-auto flex flex-col justify-center w-4/5">
        <p class="font-medium text-sm mx-auto mb-2">Bytt husstand</p>
        <Select v-if="activeStorageId" v-model="activeStorageId" class="w-full">
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
      </div>
      <div class="mx-auto">
        <RouterLink to="/inventory">
          <Button>Gå til sjekkliste og lager</Button>
        </RouterLink>
      </div>
    </div>

    <div class="w-px h-full bg-gray-300 mx-4"></div>

    <div class="w-2/3 h-full flex flex-col overflow-scroll">
      <p class="font-medium text-base mb-2">Utgår neste 3 månedene</p>
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
