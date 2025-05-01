<script setup>
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import InventoryTable from '@/components/InventoryTable.vue'
import { Button } from '@/components/ui/button/index.js'
import NewItem from '@/components/NewItem.vue'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from '@/components/ui/alert-dialog'
import { onMounted, ref, watch } from 'vue'
import axios from 'axios'

const selectedBoxIds = ref([]);

const handleDelete = () => {
  console.log(selectedBoxIds.value);
}

const activeTab = ref('matOgDrikke');
const currentItems = ref();

const items1 = [
  {
    id: 1,
    name: "Pasta",
    amount: 500,
    unit: 2,
    expirationDate: "2025-05-04"
  },
  {
    id: 2,
    name: "Rice",
    amount: 1000,
    unit: 2,
    expirationDate: "2025-05-07"
  },
  {
    id: 6,
    name: "Tomato",
    amount: 500,
    unit: 2,
    expirationDate: "2025-05-04"
  },
  {
    id: 5,
    name: "Tomato",
    amount: 400,
    unit: 2,
    expirationDate: "2025-05-17"
  },
  {
    id: 4,
    name: "Olive Oil",
    amount: 0.5,
    unit: 3,
    expirationDate: "2025-05-10"
  },
  {
    id: 7,
    name: "Ost",
    amount: 500,
    unit: 2,
    expirationDate: "2025-05-10"
  },
  {
    id: 10,
    name: "Ost",
    amount: 250,
    unit: 2,
    expirationDate: "2025-05-05"
  }
];


const items2 = [
  {
    id: 1,
    name: "LED Bulb",
    amount: 10,
    unit: 1,
    expirationDate: "2025-05-30"
  },
  {
    id: 2,
    name: "Gas Heater",
    amount: 1,
    unit: 1,
    expirationDate: "2025-06-30"
  },
  {
    id: 3,
    name: "Battery Pack",
    amount: 5,
    unit: 1,
    expirationDate: "2025-05-20"
  }
];

const items3 = [
  {
    id: 1,
    name: "User Manual for Heater",
    amount: 1,
    unit: 1,
    expirationDate: "2026-04-30"
  },
  {
    id: 2,
    name: "Instruction Book for Pasta Cooker",
    amount: 1,
    unit: 1,
    expirationDate: "2026-04-30"
  },
  {
    id: 3,
    name: "Safety Instructions for Gas Heater",
    amount: 1,
    unit: 1,
    expirationDate: "2025-10-30"
  }
];

const items4 = [
  {
    id: 1,
    name: "Pain Reliever (Ibuprofen)",
    amount: 20,
    unit: 1,
    expirationDate: "2025-05-15"
  },
  {
    id: 2,
    name: "Toothpaste",
    amount: 1,
    unit: 1,
    expirationDate: "2025-06-01"
  },
  {
    id: 3,
    name: "Shampoo",
    amount: 200,
    unit: 2,
    expirationDate: "2025-06-10"
  },
  {
    id: 4,
    name: "Hand Sanitizer",
    amount: 100,
    unit: 2,
    expirationDate: "2025-05-10"
  }
];

const items5 = [];

const storageId = ref();

const fetchItems = async () => {
  if (activeTab.value === 'matOgDrikke') {
    storageId.value = 1;
    currentItems.value = groupItems(items1);
  } else if (activeTab.value === 'varmeOgLys') {
    storageId.value = 2;
    currentItems.value = groupItems(items2);
  } else if (activeTab.value === 'informasjon') {
    storageId.value = 3;
    currentItems.value = groupItems(items3);
  } else if (activeTab.value === 'legemidOgHygiene') {
    storageId.value = 4;
    currentItems.value = groupItems(items4);
  } else if (activeTab.value === 'annet') {
    storageId.value = 5;
    currentItems.value = groupItems(items5);
  }

  /*const response = await axios.get(`http://localhost:8080/api/item/storage/${storageId.value}/items`,
    {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      params: {
        typeId: storageId.value
      }
    }
  );
  items.value = response.data;

   */
}

const groupItems = (items) => {
  const result = [];
  const totItemAmount = items.length;
  let itemCount = 0;

  while (itemCount < totItemAmount) {
    const currentItem = items[itemCount];
    const group = [currentItem];

    let subItemCount = itemCount + 1;

    while ((subItemCount < totItemAmount) && (items[subItemCount].name === currentItem.name)) {
      group.push(items[subItemCount]);
      subItemCount++;
    }

    if (group.length === 1) {
      result.push(currentItem);
    } else {
      result.push({
        name: currentItem.name,
        amount: group.reduce((sum, item) => sum + item.amount, 0),
        unit: currentItem.unit,
        expirationDate:
          group.reduce((min, item) =>
              item.expirationDate < min ? item.expirationDate : min,
            group[0].expirationDate
          ),
        items: group
      });
    }
    itemCount = subItemCount;
  }
  return result;
}

watch(() => activeTab.value, async () => {
  selectedBoxIds.value = [];
  await fetchItems();
})

onMounted(async () => {
  await fetchItems();
})
</script>

<template>
  <div class="m-auto">
    <p>[Husstand-navn] sitt beredskapslager</p>

    <div class="flex justify-end pt-2 pb-2">
      <NewItem v-if="storageId" :storageId="storageId"/>
      <AlertDialog>
        <AlertDialogTrigger :disabled="selectedBoxIds.length === 0" class="disabled:cursor-not-allowed">
          <Button
            :disabled="selectedBoxIds.length === 0"
            class="ml-2 bg-red-800 hover:bg-red-900 selected"
            @click="handleDelete">Slett markerte</Button>
        </AlertDialogTrigger>
        <AlertDialogContent v-if="selectedBoxIds.length > 0">
          <AlertDialogHeader>
            <AlertDialogTitle>Er du sikker på at du ønsker å slette?</AlertDialogTitle>
            <AlertDialogDescription>
              Denne handlingen kan ikke angres. Dette vil slette alle markerte varer fra ditt digitale lager.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Avbryt</AlertDialogCancel>
            <AlertDialogAction class="bg-red-800 hover:bg-red-900">Slett</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>

    <Tabs v-model="activeTab" v-if="currentItems" class="w-full max-w-5xl">
      <TabsList>
        <TabsTrigger value="matOgDrikke">Mat og drikke</TabsTrigger>
        <TabsTrigger value="varmeOgLys">Varme og lys</TabsTrigger>
        <TabsTrigger value="informasjon">Informasjon</TabsTrigger>
        <TabsTrigger value="legemidOgHygiene">Legemidler og hygiene</TabsTrigger>
        <TabsTrigger value="annet">Annet</TabsTrigger>
      </TabsList>

      <TabsContent value="matOgDrikke">
        <InventoryTable :newItems="currentItems" :storageId="1" tab="matOgDrikke" @selectionChanged="selectedBoxIds = $event"/>
      </TabsContent>

      <TabsContent value="varmeOgLys">
        <InventoryTable :newItems="currentItems" :storageId="2" tab="varmeOgLys" @selectionChanged="selectedBoxIds = $event"/>
      </TabsContent>

      <TabsContent value="informasjon">
        <InventoryTable :newItems="currentItems" :storageId="3" tab="informasjon" @selectionChanged="selectedBoxIds = $event"/>
      </TabsContent>

      <TabsContent value="legemidOgHygiene">
        <InventoryTable :newItems="currentItems" :storageId="4" tab="legemidOgHygiene" @selectionChanged="selectedBoxIds = $event"/>
      </TabsContent>

      <TabsContent value="annet">
        <InventoryTable :newItems="currentItems" :storageId="5" tab="annet" @selectionChanged="selectedBoxIds = $event"/>
      </TabsContent>
    </Tabs>
  </div>
</template>

<style scoped></style>
