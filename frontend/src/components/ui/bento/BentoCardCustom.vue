<template>
  <router-link
    :to="href"
    :class="[
      'relative col-span-1 flex flex-col justify-between overflow-hidden rounded-xl',
      'bg-white [box-shadow:0_0_0_1px_rgba(0,0,0,.03),0_2px_4px_rgba(0,0,0,.05),0_12px_24px_rgba(0,0,0,.05)]',
      'transform-gpu dark:bg-black dark:[border:1px_solid_rgba(255,255,255,.1)] dark:[box-shadow:0_-20px_80px_-20px_#ffffff1f_inset]',
      customClass
    ]"
  >
    <div class="absolute top-0 left-0 p-4 text-xl font-semibold text-neutral-700 dark:text-neutral-300 z-10">
      {{ name }}
    </div>

    <div class="relative mt-10 p-4 z-0">
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
    href: String
  }
}
</script>

<style scoped>
</style>
