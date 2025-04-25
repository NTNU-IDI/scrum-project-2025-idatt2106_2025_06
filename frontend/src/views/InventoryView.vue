<script setup>
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import InventoryTable from '@/components/InventoryTable.vue'
</script>

<template>
  <div class="m-auto">
    <p>Lager</p>

    <Tabs default-value="matOgDrikke" class="w-full max-w-5xl">
      <TabsList>
        <TabsTrigger value="matOgDrikke">Mat og drikke</TabsTrigger>
        <TabsTrigger value="varmeOgLys">Varme og lys</TabsTrigger>
        <TabsTrigger value="informasjon">Informasjon</TabsTrigger>
        <TabsTrigger value="legemidOgHygiene">Legemidler og hygiene</TabsTrigger>
      </TabsList>

      <TabsContent value="matOgDrikke">
        <InventoryTable tab="matOgDrikke"/>
      </TabsContent>

      <TabsContent value="varmeOgLys">
        <InventoryTable tab="varmeOgLys"/>
      </TabsContent>

      <TabsContent value="informasjon">
        <InventoryTable tab="informasjon"/>
      </TabsContent>

      <TabsContent value="legemidOgHygiene">
        <InventoryTable tab="legemidOgHygiene"/>
      </TabsContent>
    </Tabs>
  </div>
</template>

<style scoped></style>
