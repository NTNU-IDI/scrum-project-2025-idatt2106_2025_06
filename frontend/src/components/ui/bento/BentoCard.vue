<template>
  <!-- Hele BentoCard er nå en router-link yay-->
  <router-link
    :to="href"
    :class="[
      'group relative col-span-1 flex flex-col justify-between overflow-hidden rounded-xl',
      bgColor,
      '[box-shadow:0_0_0_1px_rgba(0,0,0,.03),0_2px_4px_rgba(0,0,0,.05),0_12px_24px_rgba(0,0,0,.05)]',
      'transform-gpu dark:bg-black dark:[border:1px_solid_rgba(255,255,255,.1)] dark:[box-shadow:0_-20px_80px_-20px_#ffffff1f_inset]',
      customClass
    ]"
  >
    <div>
      <div :class="['absolute inset-0 transition-all duration-300', bgColor, hoverColor]" />
    </div>
    <div class="pointer-events-none z-10 flex transform-gpu flex-col gap-1 p-12 transition-all duration-300 group-hover:-translate-y-10">
      <component :is="Icon" class="h-12 w-12 origin-left transform-gpu text-neutral-700 transition-all duration-300 ease-in-out group-hover:scale-75" />
      <h3 :class="['text-xl font-semibold text-neutral-700', nameColor]">{{ name }}</h3>
      <p :class="['max-w-lg', descColor]">{{ description }}</p>
    </div>
    <div class="pointer-events-none absolute bottom-0 flex w-full translate-y-10 transform-gpu flex-row items-center p-4 opacity-0 transition-all duration-300 group-hover:translate-y-0 group-hover:opacity-100">
      <!-- CTA tekst på kortet -->
      <span :class="['text-sm', readmoreColor, 'hover:underline']">
        {{ cta }}
      </span>
    </div>
    <div class="pointer-events-none absolute inset-0 transform-gpu transition-all duration-300 group-hover:bg-black/[.03] group-hover:dark:bg-neutral-800/10" />

    <!-- Slot for eventuelle tilpassede innhold -->
    <div class="absolute inset-0 p-4">
      <slot name="alert"></slot>
    </div>
  </router-link>
</template>

<script>
export default {
  name: "BentoCard",
  props: {
    name: String,
    customClass: String,
    background: Object,
    Icon: [Object, Function, String],
    description: String,
    href: String,  // Brukes nå som 'to' for router-link
    cta: String,
    bgColor: {
      type: String,
      default: 'bg-white' // ← fallback
    },
    descColor: {
      type: String,
      default: 'text-neutral-400'
    },
    hoverColor: {
      type: String,
      default: 'group-hover:bg-neutral-100' // fallback hover
    },
    nameColor: {
      type: String,
      default: 'dark:text-neutral-300' //fallback name text color
    },
    readmoreColor: {
      type: String,
      default: 'text-blue-600'
    }
  }
}
</script>
