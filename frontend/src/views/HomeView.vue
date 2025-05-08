<script setup>
import { BentoCard, BentoCardCustom, BentoGrid } from '@/components/ui/bento'
import AlertCard from '@/components/AlertCard.vue'
import ExpiringSoon from '@/components/ExpiringSoon.vue'
import { ArrowRightIcon } from 'lucide-vue-next'
import { useSessionStore } from '@/stores/session.js'
import { computed, onMounted, ref } from 'vue'
import { Button } from '@/components/ui/button/index.js'
import router from '@/router/router.js'
import { useStorageStore } from '@/stores/storage.js'

const sessionStore = useSessionStore();
const storageStore = useStorageStore();

const userLoggedIn = computed(() => sessionStore.token);
const storages = computed(() => storageStore.storages);

const startupFinished = ref(false);

onMounted(async () => {
  if (sessionStore.isAuthenticated) {
    try {
      await storageStore.fetchAll(sessionStore.token)
    } catch (error) {
      console.error("Klarte ikke hente husstander:", error)
    }
  }

  startupFinished.value = true;
})
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
        href="/alerts"
        name="Nyheter"
      >
        <div class="h-[25rem] overflow-y-auto flex flex-col gap-2">
          <!-- TODO: Implementere dynamisk generering av alerts fra databasen -->
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
          <AlertCard description="lolololol" severity="red" time="Nå" title="lol" variant="short" />
        </div>
      </BentoCardCustom>

      <BentoCardCustom customClass="col-span-3 min-h-[30rem] w-[45rem]" class="cursor-default" name="Beredskap">
        <div>
          <div v-if="!userLoggedIn" class="flex flex-col h-[20rem] gap-3 justify-center">
            <p class="text-lg text-center font-bold">Du må være innlogget for å se beredskap og utgår snart.</p>
            <Button class="mx-auto" @click="router.push('/login')">Logg inn</Button>
          </div>
          <div v-else-if="startupFinished && storages.length === 0" class="flex flex-col h-[20rem] gap-3 justify-center">
            <p class="text-lg font-bold text-center"> Finner ingen husstander </p>
            <p class="text-center">For å se lager må du være med i en husstand eller opprett en egen.</p>
            <Button @click="router.push('/profile')" class="mx-auto">Bli med i husstand</Button>
          </div>
          <div v-else-if="startupFinished && storages.length > 0">
            <ExpiringSoon/>
          </div>
        </div>
      </BentoCardCustom>
    </BentoGrid>
  </div>
</template>

<style scoped></style>
