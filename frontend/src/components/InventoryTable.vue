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
import { Checkbox } from '@/components/ui/checkbox/index.js'
import { ChevronDown, ChevronRight } from 'lucide-vue-next';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger
} from '@/components/ui/tooltip/index.js'
import EditItem from '@/components/EditItem.vue'


const props = defineProps({
  tab: { type: String, required: true },
});

const selectedBoxes = ref(new Set());
const selectAllBoxes = ref(false);

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
      name: "Tomato",
      amount: "700 g",
      daysLeft: 2,
      items: [
        { "id": 6, "name": "Pasta", "amount": "500 g", "daysLeft": 4 },
        { "id": 5, "name": "Pasta", "amount": "200 g", "daysLeft": 2 }
      ]
    },
    {
      id: 4,
      name: "Olive Oil",
      amount: "500 ml",
      daysLeft: 10
    },
    {
      name: "Ost",
      amount: "750 g",
      daysLeft: 5,
      items: [
        { "id": 7, "name": "Pasta", "amount": "500 g", "daysLeft": 10 },
        { "id": 10, "name": "Pasta", "amount": "250 g", "daysLeft": 5 }
      ]
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
const selectedAllSubBoxes = (subItems) => {
  return subItems.every(item => selectedBoxes.value.has(item.id));
};

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

const toggleAll = (value) => {
  selectAllBoxes.value = value;

  if (value) {
    const allIds = new Set();
    currentItems.value.forEach(item => {
      allIds.add(item.id);
      if (item.items) {
        item.items.forEach(subItem => {
          allIds.add(subItem.id);
        });
      }
    });
    selectedBoxes.value = allIds;
  } else {
    selectedBoxes.value.clear();
  }
};

const toggleItem = (isChecked, id) => {
  if (isChecked) {
    selectedBoxes.value.add(id);
  } else {
    selectedBoxes.value.delete(id);
  }

  // Update selectAll status
  const totalItems = currentItems.value.reduce((count, item) => {
    count += 1;
    if (item.items) count += item.items.length;
    return count;
  }, 0);

  selectAllBoxes.value = selectAllBoxes.value.size === totalItems;
};

const toggleAllSubItem = (isChecked, items) => {
  if (isChecked) {
    items.forEach(item => selectedBoxes.value.add(item.id));
  } else {
    // If selectAll is false, remove all sub-item IDs from checkedIds
    items.forEach(item => selectedBoxes.value.delete(item.id));
  }
}


onMounted(() => {
  fetchItems();
})
</script>

<template>
    <Table>
      <TableHeader>
        <TableRow class="bg-gray-100">
          <TableHead></TableHead>
          <TableHead class="w-[100px]">Vare</TableHead>
          <TableHead class="text-center">Total mengde</TableHead>
          <TableHead class="text-center">Utløpsdato</TableHead>
          <TableHead></TableHead>
          <TableHead>
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger>
                  <Checkbox v-model="selectAllBoxes" @update:modelValue="toggleAll"/>
                </TooltipTrigger>
                <TooltipContent>
                  <p>Marker alle</p>
                </TooltipContent>
              </Tooltip>
            </TooltipProvider>
          </TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <Collapsible v-slot="{ open }" v-for="item in currentItems" :key="item.id" as="template">
        <TableRow>
          <TableCell>
            <CollapsibleTrigger as="button" v-if="item.items">
              <ChevronRight v-if="!open" />
              <ChevronDown v-if="open" />
            </CollapsibleTrigger>
          </TableCell>
          <TableCell class="font-medium">{{ item.name }}</TableCell>
          <TableCell class="text-center">{{ item.amount}}</TableCell>
          <TableCell class="text-center">{{ item.daysLeft}} dager igjen</TableCell>
          <TableCell>
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger>
                  <EditItem v-if="!item.items" />
                </TooltipTrigger>
                <TooltipContent>
                  <p>Endre</p>
                </TooltipContent>
              </Tooltip>
            </TooltipProvider>
          </TableCell>
          <TableCell>
            <Checkbox
              v-if="item.items"
              :model-value="selectedAllSubBoxes(item.items)"
              @update:modelValue="val => toggleAllSubItem(val, item.items)"
            />
            <Checkbox
              v-else-if="!item.items"
              :model-value="selectedBoxes.has(item.id)"
              @update:modelValue="val => toggleItem(val, item.id)"
            />
          </TableCell>
        </TableRow>
        <CollapsibleContent as="template" v-if="item.items">
          <TableRow v-for="item in item.items" :key="item.id" class="bg-gray-50 hover:bg-gray-100">
            <TableCell></TableCell>
            <TableCell class="font-medium"></TableCell>
            <TableCell class="text-center">{{ item.amount}}</TableCell>
            <TableCell class="text-center">{{ item.daysLeft}} dager igjen</TableCell>
            <TableCell>
              <TooltipProvider>
              <Tooltip>
                <TooltipTrigger>
                  <EditItem />
                </TooltipTrigger>
                <TooltipContent>
                  <p>Edit</p>
                </TooltipContent>
              </Tooltip>
              </TooltipProvider>
            </TableCell>
            <TableCell>
              <Checkbox
                :model-value="selectedBoxes.has(item.id)"
                @update:modelValue="val => toggleItem(val, item.id)"
              />
            </TableCell>
          </TableRow>
        </CollapsibleContent>
        </Collapsible>
      </TableBody>
    </Table>
</template>

<style scoped>

</style>
