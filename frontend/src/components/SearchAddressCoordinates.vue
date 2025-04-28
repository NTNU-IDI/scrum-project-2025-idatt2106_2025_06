<script setup>

import { Tabs, TabsList, TabsContent, TabsTrigger } from '@/components/ui/tabs/index.js'
import {
  Card,
  CardContent,
  CardFooter,

} from '@/components/ui/card/index.js'
import { Label } from '@/components/ui/label/index.js'
import { Button } from '@/components/ui/button/index.js'
</script>

<template>
  <Tabs default-value="address" class="w-[400px]">
    <TabsList class="grid w-full grid-cols-2">
      <TabsTrigger value="address">
        Adresse
      </TabsTrigger>
      <TabsTrigger value="coordinates">
        Koordinater
      </TabsTrigger>
    </TabsList>

    <!-- Adresse-tab -->
    <TabsContent value="address">
      <Card>
        <CardContent class="space-y-2">
          <div class="space-y-1">
            <Label for="latitude">Adresse</Label>
            <Input class="border w-full" id="address" placeholder="Karl Johans gate 1, Oslo" />
          </div>
        </CardContent>
        <CardFooter>
          <Button class="w-full">Søk</Button>
        </CardFooter>
      </Card>
    </TabsContent>

    <!-- Koordinater-tab -->
    <TabsContent value="coordinates">
      <Card>
        <CardContent class="space-y-2">
          <div class="space-y-1">
            <Label for="latitude">Breddegrad</Label>
            <Input class="border w-full" id="latitude" placeholder="59.9139" />
          </div>
          <div class="space-y-1">
            <Label for="longitude">Lengdegrad</Label>
            <Input class="border w-full" id="longitude" placeholder="10.7522" />
          </div>
        </CardContent>
        <CardFooter>
          <Button class="w-full">Søk</Button>
        </CardFooter>
      </Card>
    </TabsContent>
  </Tabs>
</template>

<style scoped>
.border{
  border: 1px solid #D1D5DB;
  padding: 3px;
  margin: 10px;
  border-radius: 0.5rem;
}
</style>
