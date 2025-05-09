<script setup>
import { cn } from '@/lib/utils';
import {
  ChartSingleTooltip,
  defaultColors,
} from '@/components/ui/chart';
import { VisDonut, VisSingleContainer } from '@unovis/vue';
import { useMounted } from '@vueuse/core';
import { computed } from 'vue';

const props = defineProps({
  data: { type: Array, required: true },
  colors: { type: Array, required: false },
  index: { type: null, required: true },
  margin: {
    type: null,
    required: false,
    default: () => ({ top: 0, bottom: 0, left: 0, right: 0 }),
  },
  showLegend: { type: Boolean, required: false, default: true },
  showTooltip: { type: Boolean, required: false, default: true },
  filterOpacity: { type: Number, required: false, default: 0.2 },
  category: { type: String, required: true },
  type: { type: String, required: false, default: 'donut' },
  sortFunction: { type: Function, required: false, default: () => undefined },
  valueFormatter: { type: Function, required: false },
  customTooltip: { type: null, required: false },
});

const valueFormatter = props.valueFormatter ?? ((tick) => `${tick}`);
const category = computed(() => props.category);

const isMounted = useMounted();
const colors = computed(() =>
  props.colors?.length
    ? props.colors
    : defaultColors(
        props.data.filter((d) => d[props.category]).filter(Boolean).length,
      ),
);

const doneValue = computed(() => {
  return props.data[0].total;
});
</script>

<template>
  <div :class="cn('w-full h-40 flex flex-col items-end', $attrs.class ?? '')">
    <VisSingleContainer
      :style="{ height: isMounted ? '100%' : 'auto' }"
      :margin="{ left: 20, right: 20 }"
      :data="data"
    >
      <VisDonut
        :value="(d) => d[category]"
        :sort-function="sortFunction"
        :color="colors"
        :arc-width="type === 'donut' ? 15 : 0"
        :show-background="false"
        :central-label="type === 'donut' ? valueFormatter(doneValue) + '%' : ''"
      />

      <slot />
    </VisSingleContainer>
  </div>
</template>
