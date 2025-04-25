<template>
  <!-- Hele BentoCard er nå en router-link yay-->
  <router-link
    :to="href"
    :class="[
      'relative col-span-1 flex flex-col justify-between overflow-hidden rounded-xl',
      'bg-white [box-shadow:0_0_0_1px_rgba(0,0,0,.03),0_2px_4px_rgba(0,0,0,.05),0_12px_24px_rgba(0,0,0,.05)]',
      'transform-gpu dark:bg-black dark:[border:1px_solid_rgba(255,255,255,.1)] dark:[box-shadow:0_-20px_80px_-20px_#ffffff1f_inset]',
      customClass
    ]"
  >
    <!-- Tittel plassert i øverste venstre hjørne -->
    <div class="absolute top-0 left-0 p-4 text-xl font-semibold text-neutral-700 dark:text-neutral-300 z-10">
      {{ name }}
    </div>

    <!-- Slot for custom content (som alerts eller annet innhold), plassert under tittelen -->
    <div class="relative mt-10 p-4 z-0"> <!-- mt-12 for å legge plass til tittelen -->
      <slot></slot>
    </div>
  </router-link>
</template>

<script>
export default {
  name: "BentoCard",
  props: {
    name: String,
    customClass: String,
    href: String  // Sørg for å ta inn href som en prop, slik at vi kan bruke den med router-link
  }
}
</script>

<style scoped>
/* Her kan du legge til annen styling hvis nødvendig */
</style>
