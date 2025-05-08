<script setup>
import { BentoCard, BentoCardCustom, BentoGrid } from '@/components/ui/bento'
import AlertCard from '@/components/AlertCard.vue'
import { RouterLink } from 'vue-router'
import { useWebSocketStore } from '@/stores/websocket.js'
const webSocketStore = useWebSocketStore();
const alerts = webSocketStore.alerts;
import ExpiringSoon from '@/components/ExpiringSoon.vue'
import { ArrowRightIcon } from 'lucide-vue-next'
</script>

<template>
  <div class="m-auto flex justify-center">
    <BentoGrid class="my-auto">
      <BentoCard
        :Icon="ArrowRightIcon"
        cta="Les mer"
        customClass="col-span-2"
        description="Les om forskjellige krisescenarioer"
        href="/scenario"
        name="Krisescenarioer"
      />

      <BentoCard
        :Icon="ArrowRightIcon"
        cta="Les mer"
        customClass="col-span-2"
        description="Generell info om krisehåndtering (før/under/etter)"
        href="/info"
        name="Krisehåndtering"
      >
      </BentoCard>

      <BentoCardCustom
        customClass="col-span-1 min-h-[30rem] group hover:group-hover:bg-transparent"
        name="Varslinger"
        url="/"
      >
        <div class="flex flex-col gap-2">
          <template v-for="(alert, index) in alerts" :key="index">
            <RouterLink :to="'alerts/'" class="relative">
              <AlertCard
                :description="alert.description"
                :severity="alert.severity"
                :time="alert.time"
                :title="alert.title"
                variant="short"
              />
            </RouterLink>
          </template>
        </div>
      </BentoCardCustom>

      <BentoCardCustom customClass="col-span-3 min-h-[30rem] w-[45rem]" class="cursor-default" name="Beredskap">
        <ExpiringSoon/>
      </BentoCardCustom>
    </BentoGrid>
  </div>
</template>

<style scoped></style>
