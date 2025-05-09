<script setup>
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import InventoryTable from '@/components/InventoryTable.vue'
import { Button } from '@/components/ui/button/index.js'
import NewItem from '@/components/NewItem.vue'
import { useWindowSize } from '@vueuse/core'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger
} from '@/components/ui/alert-dialog'
import { computed, onMounted, ref, watch } from 'vue'
import {
  Select,
  SelectContent,
  SelectGroup, SelectItem,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select/index.js'
import { RefreshCw } from 'lucide-vue-next'
import { useStorageStore } from '@/stores/storage.js'
import router from '@/router/router.js'
import { useSessionStore } from '@/stores/session.js'
import { useInventoryStore } from '@/stores/inventory.js'
import Checklist from '@/components/Checklist.vue'

const selectedBoxIds = ref([])

const handleDelete = async () => {
  await inventoryStore.deleteAllSelectedItems(selectedBoxIds.value, activeStorageId.value, typeId.value)
}

const activeTab = ref('matOgDrikke')
const currentItems = computed(() => groupItems(inventoryStore.inventory))

const types = [
  { id: 'matOgDrikke', name: 'Mat og drikke' },
  { id: 'varmeOgLys', name: 'Varme og lys' },
  { id: 'informasjon', name: 'Informasjon' },
  { id: 'legemidOgHygiene', name: 'Legemidler og hygiene' },
  { id: 'annet', name: 'Annet' }
]

const typeId = computed(() => {
  switch (activeTab.value) {
    case 'matOgDrikke':
      return 1
    case 'varmeOgLys':
      return 2
    case 'informasjon':
      return 3
    case 'legemidOgHygiene':
      return 4
    case 'annet':
      return 5
    default:
      return 1
  }
})
const isLoading = ref(false)

const inventoryStore = useInventoryStore()

const fetchItems = async () => {
  isLoading.value = true

  await inventoryStore.getAllFromInventoryType(activeStorageId.value, typeId.value)

  isLoading.value = false
}

const totItemAmount = ref(0)
const storageStore = useStorageStore()
const allStorages = computed(() => storageStore.storages)
const activeStorageId = ref()
const activeStorageName = ref()

const refreshItems = async () => {
  isLoading.value = true
  await fetchItems()
  isLoading.value = false
}

const groupItems = (items) => {
  const result = []
  totItemAmount.value = items.length
  let itemCount = 0

  while (itemCount < totItemAmount.value) {
    const currentItem = items[itemCount]
    const group = [currentItem]

    let subItemCount = itemCount + 1

    while ((subItemCount < totItemAmount.value) && (items[subItemCount].name === currentItem.name)) {
      group.push(items[subItemCount])
      subItemCount++
    }

    if (group.length === 1) {
      result.push(currentItem)
    } else {
      result.push({
        name: currentItem.name,
        amount: group.reduce((sum, item) => sum + item.amount, 0),
        unitId: currentItem.unitId,
        expiryDate:
          group.reduce((min, item) =>
              item.expiryDate < min ? item.expiryDate : min,
            group[0].expiryDate
          ),
        items: group
      })
    }
    itemCount = subItemCount
  }
  return result
}

const sessionStore = useSessionStore()
const startupFinished = ref(false)
const smallerScreenTab = ref('inventory')

const { width: screenWidth } = useWindowSize()
const isSmallerScreen = computed(() => screenWidth.value < 1280)

watch(() => activeTab.value, async () => {
  selectedBoxIds.value = []
  await fetchItems()
})

watch(() => activeStorageId.value, async (newId) => {
  const newSelection = allStorages.value.find(storage => storage.id === newId)
  activeStorageName.value = newSelection.name

  selectedBoxIds.value = []
  await fetchItems()
})

onMounted(async () => {
  if (!sessionStore.isAuthenticated) {
    router.push('/login')
    console.log('Det er noe galt med innloggingen, tas til logg inn')
  }

  try {
    await storageStore.fetchAll(sessionStore.token)
  } catch (error) {
    console.error('Klarte ikke hente husstander:', error)
  }

  if (allStorages.value.length > 0) {
    activeStorageId.value = allStorages.value[0].id
    activeStorageName.value = allStorages.value[0].name
  }
  await fetchItems()
  startupFinished.value = true
})
</script>

<template>
  <div class="m-auto">
    <div v-if="isSmallerScreen && activeStorageId" class="mx-auto my-5">
      <Tabs v-model="smallerScreenTab">
        <TabsList>
          <TabsTrigger value="inventory">Beredskapslager</TabsTrigger>
          <TabsTrigger value="checklist">Beredskapsgrad</TabsTrigger>
        </TabsList>
      </Tabs>
    </div>
    <div v-if="activeStorageId" class="mx-auto flex">
      <div v-if="(smallerScreenTab === 'checklist' && isSmallerScreen ) || !isSmallerScreen"
           class="xl:mr-10">
        <Checklist />
      </div>
      <div v-if="(smallerScreenTab === 'inventory' && isSmallerScreen ) || !isSmallerScreen"
           class="mx-auto my-4 xl:mt-6">
        <div class="flex flex-col md:flex-row justify-between pt-2 pb-2 items-center">
          <p class="text-xl font-bold"> {{ activeStorageName }} sitt beredskapslager</p>
          <div class="flex flex-row gap-2 mt-2 w-11/12 md:justify-end">
            <Select v-if="activeStorageId" v-model="activeStorageId">
              <SelectTrigger class="w-1/2">
                <SelectValue placeholder="Velg sted" />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  <SelectItem v-for="storage in allStorages" :key="storage.id" :value="storage.id">
                    {{ storage.name }}
                  </SelectItem>
                </SelectGroup>
              </SelectContent>
            </Select>
            <Select v-if="currentItems && allStorages.length > 0 && screenWidth < 768"
                    v-model="activeTab">
              <SelectTrigger class="w-1/2">
                <SelectValue placeholder="Velg type" />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  <SelectItem v-for="type in types" :key="type.id" :value="type.id">
                    {{ type.name }}
                  </SelectItem>
                </SelectGroup>
              </SelectContent>
            </Select>
          </div>
        </div>

        <div class="flex justify-center md:justify-end pt-2 pb-2">
          <Button :disabled="isLoading" class="mr-2" variant="outline" @click="refreshItems">
            <RefreshCw :class="isLoading ? 'animate-spin' : '' " />
          </Button>
          <NewItem v-if="typeId" :storageId="activeStorageId" :typeId="typeId" />
          <AlertDialog>
            <AlertDialogTrigger :disabled="selectedBoxIds.length === 0"
                                class="disabled:cursor-not-allowed">
              <Button
                :disabled="selectedBoxIds.length === 0"
                class="ml-2"
                variant="destructive">
                Slett markerte
              </Button>
            </AlertDialogTrigger>
            <AlertDialogContent v-if="selectedBoxIds.length > 0">
              <AlertDialogHeader>
                <AlertDialogTitle>Er du sikker på at du ønsker å slette?</AlertDialogTitle>
                <AlertDialogDescription>
                  Denne handlingen kan ikke angres. Dette vil slette alle markerte varer fra ditt
                  digitale lager.
                </AlertDialogDescription>
              </AlertDialogHeader>
              <AlertDialogFooter>
                <AlertDialogCancel>Avbryt</AlertDialogCancel>
                <AlertDialogAction class="bg-red-800 hover:bg-red-900" @click="handleDelete">Slett
                </AlertDialogAction>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialog>
        </div>

        <Tabs v-if="currentItems && allStorages.length > 0 && screenWidth >= 768"
              v-model="activeTab">
          <TabsList>
            <TabsTrigger value="matOgDrikke">Mat og drikke</TabsTrigger>
            <TabsTrigger value="varmeOgLys">Varme og lys</TabsTrigger>
            <TabsTrigger value="informasjon">Informasjon</TabsTrigger>
            <TabsTrigger value="legemidOgHygiene">Legemidler og hygiene</TabsTrigger>
            <TabsTrigger value="annet">Annet</TabsTrigger>
          </TabsList>

          <TabsContent value="matOgDrikke">
            <InventoryTable :newItems="currentItems" :storageId="activeStorageId" :totItemAmount="totItemAmount"
                            :typeId="1"
                            @selectionChanged="selectedBoxIds = $event" />
          </TabsContent>

          <TabsContent value="varmeOgLys">
            <InventoryTable :newItems="currentItems" :storageId="activeStorageId" :totItemAmount="totItemAmount"
                            :typeId="2"
                            @selectionChanged="selectedBoxIds = $event" />
          </TabsContent>

          <TabsContent value="informasjon">
            <InventoryTable :newItems="currentItems" :storageId="activeStorageId" :totItemAmount="totItemAmount"
                            :typeId="3"
                            @selectionChanged="selectedBoxIds = $event" />
          </TabsContent>

          <TabsContent value="legemidOgHygiene">
            <InventoryTable :newItems="currentItems" :storageId="activeStorageId" :totItemAmount="totItemAmount"
                            :typeId="4"
                            @selectionChanged="selectedBoxIds = $event" />
          </TabsContent>

          <TabsContent value="annet">
            <InventoryTable :newItems="currentItems" :storageId="activeStorageId" :totItemAmount="totItemAmount"
                            :typeId="5"
                            @selectionChanged="selectedBoxIds = $event" />
          </TabsContent>
        </Tabs>

        <div v-if="screenWidth < 768">
          <InventoryTable :newItems="currentItems" :storageId="activeStorageId" :totItemAmount="totItemAmount"
                          :typeId="typeId"
                          @selectionChanged="selectedBoxIds = $event" />
        </div>
      </div>
    </div>
    <div v-else-if="startupFinished && allStorages.length === 0"
         class="flex flex-col m-auto mt-10 items-center">
      <p class="text-xl font-bold m-1 text-center"> Finner ingen husstander </p>
      <p class="m-1 text-center">For å se lager må du være med i en husstand eller opprett en
        egen.</p>
      <RouterLink class="my-2" to="/profile">
        <Button class="">Bli med i husstand</Button>
      </RouterLink>
    </div>
  </div>
</template>

<style scoped></style>
