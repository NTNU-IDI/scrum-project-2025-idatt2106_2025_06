<script setup>

import { Button } from '@/components/ui/button/index.js'
import { Input } from '@/components/ui/input/index.js'
</script>

<template>
  <div class="flex items-center justify-center">
    <Input class="bg-white" placeholder="Søk i kartet" />
    <Button>
      Search
    </Button>
  </div>
</template>

<style scoped>
.border{
  border: 1px solid #D1D5DB;
  padding: 3px;
  margin: 10px;
  border-radius: 0.5rem;
}
</style>
