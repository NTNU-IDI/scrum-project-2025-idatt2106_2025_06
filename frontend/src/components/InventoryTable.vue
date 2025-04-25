<script setup>
import { onMounted, ref } from 'vue'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '@/components/ui/table/index.js'
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger
} from '@/components/ui/collapsible/index.js'

const props = defineProps({
  tab: { type: String, required: true },
});

//const items = ref();

const items = {
  matOgDrikke: [
    {
      id: 1,
      name: "Pasta",
      amount: "500 g",
      daysLeft: 4
    },
    {
      id: 2,
      name: "Rice",
      amount: "1 kg",
      daysLeft: 7
    },
    {
      id: 3,
      name: "Tomato Sauce",
      amount: "700 g",
      daysLeft: 2,
      items: [
        { "id": 4, "name": "Pasta", "amount": "500 g", "daysLeft": 4 },
        { "id": 5, "name": "Pasta", "amount": "200 g", "daysLeft": 2 }
      ]
    },
    {
      id: 4,
      name: "Olive Oil",
      amount: "500 ml",
      daysLeft: 10
    }
  ],
  varmeOgLys: [
    {
      id: 1,
      name: "LED Bulb",
      amount: "10 units",
      daysLeft: 30
    },
    {
      id: 2,
      name: "Gas Heater",
      amount: "1 unit",
      daysLeft: 60
    },
    {
      id: 3,
      name: "Battery Pack",
      amount: "5 units",
      daysLeft: 20
    }
  ],
  informasjon: [
    {
      id: 1,
      name: "User Manual for Heater",
      amount: "1 copy",
      daysLeft: 365
    },
    {
      id: 2,
      name: "Instruction Book for Pasta Cooker",
      amount: "1 copy",
      daysLeft: 365
    },
    {
      id: 3,
      name: "Safety Instructions for Gas Heater",
      amount: "1 copy",
      daysLeft: 180
    }
  ],
  legemidOgHygiene: [
    {
      id: 1,
      name: "Pain Reliever (Ibuprofen)",
      amount: "20 tablets",
      daysLeft: 15
    },
    {
      id: 2,
      name: "Toothpaste",
      amount: "1 tube",
      daysLeft: 30
    },
    {
      id: 3,
      name: "Shampoo",
      amount: "200 ml",
      daysLeft: 40
    },
    {
      id: 4,
      name: "Hand Sanitizer",
      amount: "100 ml",
      daysLeft: 10
    }
  ]
};

const currentItems = ref();

const fetchItems = async () => {
  let apiPath = '';

  if (props.tab === 'matOgDrikke') {
    apiPath = '/api/matOgDrikke';
    currentItems.value = items.matOgDrikke;
  } else if (props.tab === 'varmeOgLys') {
    apiPath = '/api/varmeOgLys';
    currentItems.value = items.varmeOgLys;
  } else if (props.tab === 'informasjon') {
    apiPath = '/api/informasjon';
    currentItems.value = items.informasjon;
  } else if (props.tab === 'legemidOgHygiene') {
    apiPath = '/api/legemidOgHygiene';
    currentItems.value = items.legemidOgHygiene;

  }

  //get her, bruke fetch eller axios?
  //const response = ?
  //items.value = response.data;
}

onMounted(() => {
  fetchItems();
})
</script>

<template>
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead></TableHead>
          <TableHead class="w-[100px]">Vare</TableHead>
          <TableHead class="text-center">Total mengde</TableHead>
          <TableHead class="text-center">Utløpsdato</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <Collapsible v-slot="{ open }" v-for="item in currentItems" :key="item.id" as="template">
        <TableRow>
          <TableCell>
            <CollapsibleTrigger as="button" v-if="item.items">
              {{ open ? '▼' : '▶' }}
            </CollapsibleTrigger>
          </TableCell>
          <TableCell class="font-medium">{{ item.name }}</TableCell>
          <TableCell class="text-center">{{ item.amount}}</TableCell>
          <TableCell class="text-center">{{ item.daysLeft}} dager igjen</TableCell>
        </TableRow>
        <CollapsibleContent as="template" v-if="item.items">
          <TableRow v-for="item in item.items" :key="item.id">
            <TableCell></TableCell>
            <TableCell class="font-medium"></TableCell>
            <TableCell class="text-center">{{ item.amount}}</TableCell>
            <TableCell class="text-center">{{ item.daysLeft}} dager igjen</TableCell>
          </TableRow>
        </CollapsibleContent>
        </Collapsible>
      </TableBody>
    </Table>
</template>

<style scoped>

</style>
