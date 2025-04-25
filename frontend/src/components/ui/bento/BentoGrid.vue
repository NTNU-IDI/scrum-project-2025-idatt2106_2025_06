<template>
  <div :class="['grid w-full grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4', customClass]">
    <slot />
  </div>
</template>

<script>
export default {
  name: "BentoGrid",
  props: {
    customClass: {
      type: String,
      default: ''
    }
  }
}
</script>
