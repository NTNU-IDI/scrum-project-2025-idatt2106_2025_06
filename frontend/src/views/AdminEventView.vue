<script>
import {defineComponent} from "vue";
import Map from "@/components/Map.vue";
import { Tabs, TabsList, TabsContent, TabsTrigger } from '@/components/ui/tabs/index.js'
import {
  Card,
  CardContent,
  CardDescription, CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card/index.js'
import { Label } from '@/components/ui/label/index.js'


export default defineComponent({
  data: () => ({
    location: {
      lng:  10.40574,
      lat:  63.41754,
      bearing: 0,
      pitch: 0,
      zoom: 12
    }
  }),
  components: {
    CardFooter,
    Label,
    CardContent,
    CardDescription,
    CardTitle,
    CardHeader,
    Card,
    Tabs,
    TabsList,     // <--- disse må legges til
    TabsTrigger,  // <---
    TabsContent,  // <---
    Map
  }
})

</script>

<template>
  <div class="m-auto flex relative flex-1 w-full py-10 gap-6">
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
          <CardHeader>
            <CardTitle>Adresse</CardTitle>
            <CardDescription>
              Skriv inn adressen du ønsker.
            </CardDescription>
          </CardHeader>
          <CardContent class="space-y-2">
            <div class="space-y-1">
              <Label for="address">Adresse</Label>
              <Input id="address" placeholder="F.eks. Karl Johans gate 1, Oslo" />
            </div>
          </CardContent>
          <CardFooter>
            <Button>Lagre adresse</Button>
          </CardFooter>
        </Card>
      </TabsContent>

      <!-- Koordinater-tab -->
      <TabsContent value="coordinates">
        <Card>
          <CardHeader>
            <CardTitle>Koordinater</CardTitle>
            <CardDescription>
              Skriv inn breddegrad og lengdegrad.
            </CardDescription>
          </CardHeader>
          <CardContent class="space-y-2">
            <div class="space-y-1">
              <Label for="latitude">Breddegrad</Label>
              <Input id="latitude" placeholder="F.eks. 59.9139" />
            </div>
            <div class="space-y-1">
              <Label for="longitude">Lengdegrad</Label>
              <Input id="longitude" placeholder="F.eks. 10.7522" />
            </div>
          </CardContent>
          <CardFooter>
            <Button>Lagre koordinater</Button>
          </CardFooter>
        </Card>
      </TabsContent>
    </Tabs>
    <Map v-model="location" class="flex-[2]" />
  </div>
</template>


<style scoped></style>
