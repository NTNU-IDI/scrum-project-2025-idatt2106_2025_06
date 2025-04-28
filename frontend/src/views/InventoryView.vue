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
</script>

<template>
  <div class="m-auto">
    <p>[Husstand-navn] sitt beredskapslager</p>

    <div class="flex justify-end pt-2 pb-2">
      <NewItem />
      <AlertDialog>
        <AlertDialogTrigger>
          <Button class="ml-2 bg-red-800 hover:bg-red-900">Slett markerte</Button>
        </AlertDialogTrigger>
        <AlertDialogContent>
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

    <Tabs default-value="matOgDrikke" class="w-full max-w-5xl">
      <TabsList>
        <TabsTrigger value="matOgDrikke">Mat og drikke</TabsTrigger>
        <TabsTrigger value="varmeOgLys">Varme og lys</TabsTrigger>
        <TabsTrigger value="informasjon">Informasjon</TabsTrigger>
        <TabsTrigger value="legemidOgHygiene">Legemidler og hygiene</TabsTrigger>
        <TabsTrigger value="annet">Annet</TabsTrigger>
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

      <TabsContent value="annet">
        <InventoryTable tab="annet"/>
      </TabsContent>
    </Tabs>
  </div>
</template>

<style scoped></style>
