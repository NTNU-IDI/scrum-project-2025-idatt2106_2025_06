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
  newItems: { type: Array, required: true },
  storageId: {type: Number, required: true},
  tab: { type: String }
});
const emit = defineEmits(['selectionChanged']);

const currentItems = ref();

const selectedBoxes = ref(new Set());
const areAllBoxesChecked = ref(false);
const totalItemAmount = ref(0);

const selectedAllSubBoxes = (subItems) => {
  return subItems.every(item => selectedBoxes.value.has(item.id));
};

const toggleAll = (value) => {
  areAllBoxesChecked.value = value;

  if (value) {
    currentItems.value.forEach(item => {
      if (item.items) {
        item.items.forEach(item => selectedBoxes.value.add(item.id));
      } else {
        selectedBoxes.value.add(item.id);
      }
    });
  } else {
    selectedBoxes.value.clear();
  }
  emit('selectionChanged', Array.from(selectedBoxes.value));
};

const toggleItem = (id, value) => {
  if (value) {
    selectedBoxes.value.add(id);
  } else {
    selectedBoxes.value.delete(id);
  }

  areAllBoxesChecked.value = selectedBoxes.value.size === totalItemAmount.value;
  emit('selectionChanged', Array.from(selectedBoxes.value));
};

const toggleAllSubItem = (items, value) => {
  if (value) {
    items.forEach(item => selectedBoxes.value.add(item.id));
  } else {
    items.forEach(item => selectedBoxes.value.delete(item.id));
  }

  areAllBoxesChecked.value = selectedBoxes.value.size === totalItemAmount.value;
  emit('selectionChanged', Array.from(selectedBoxes.value));
}

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
  } else {
    return dayAmount + " dager";
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

onMounted(() => {
  currentItems.value = props.newItems;
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
                  <Checkbox v-model="areAllBoxesChecked" @update:modelValue="toggleAll"/>
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
          <TableCell class="text-center">{{ getAmountAndUnit(item.amount, item.unit) }}</TableCell>
          <TableCell class="text-center" :class="getExpirationColor(item.expirationDate)">
            {{ getDaysLeft(item.expirationDate) }}
          </TableCell>
          <TableCell>
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger>
                  <EditItem v-if="!item.items" :item="item" :storageId="storageId" />
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
              @update:modelValue="value => toggleAllSubItem(item.items, value)"
            />
            <Checkbox
              v-else-if="!item.items"
              :model-value="selectedBoxes.has(item.id)"
              @update:modelValue="value => toggleItem(item.id, value)"
            />
          </TableCell>
        </TableRow>
        <CollapsibleContent as="template" v-if="item.items">
          <TableRow v-for="item in item.items" :key="item.id" class="bg-gray-50 hover:bg-gray-100">
            <TableCell></TableCell>
            <TableCell class="font-medium"></TableCell>
            <TableCell class="text-center">{{ getAmountAndUnit(item.amount, item.unit) }}</TableCell>
            <TableCell class="text-center" :class="getExpirationColor(item.expirationDate)">
              {{ getDaysLeft(item.expirationDate) }}
            </TableCell>
            <TableCell>
              <TooltipProvider>
              <Tooltip>
                <TooltipTrigger>
                  <EditItem :item="item" :storageId="storageId" />
                </TooltipTrigger>
                <TooltipContent>
                  <p>Endre</p>
                </TooltipContent>
              </Tooltip>
              </TooltipProvider>
            </TableCell>
            <TableCell>
              <Checkbox
                :model-value="selectedBoxes.has(item.id)"
                @update:modelValue="value => toggleItem(item.id, value)"
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
