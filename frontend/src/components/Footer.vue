<script lang="ts" setup>
import Onboarding from '@/components/Onboarding.vue'
</script>

<template>
  <footer class="pt-10 w-full">
    <div class="p-10 border gap-4 flex flex-col">
      <div class="grid grid-cols-2 md:grid-cols-4 xl:grid-cols-6 gap-x-12 gap-y-8">
        <div class="col-span-full xl:col-span-2">
          <a class="flex font-bold items-center gap-3" href="/">
            <img alt="logo" class="h-10 w-10" src="/krisefikserLogo.svg" />
            <h3 class="text-2xl font-bold">KRISEFIKSER</h3>
          </a>
        </div>

        <div class="flex flex-col gap-2">
          <h3 class="font-bold text-lg">Sider</h3>
          <div>
            <router-link class="opacity-60 hover:opacity-100" to="/"> Forside</router-link>
          </div>

          <div>
            <router-link class="opacity-60 hover:opacity-100" to="/alerts"> Varslinger</router-link>
          </div>

          <div>
            <router-link class="opacity-60 hover:opacity-100" to="/inventory">
              Beredskapslager
            </router-link>
          </div>

          <div>
            <router-link class="opacity-60 hover:opacity-100" to="/map"> Kart</router-link>
          </div>

          <div>
            <router-link class="opacity-60 hover:opacity-100" to="/profile"> Min side</router-link>
          </div>
        </div>

        <div class="flex flex-col gap-2">
          <h3 class="font-bold text-lg">Personvern og info</h3>
          <div>
            <router-link class="opacity-60 hover:opacity-100" to="/privacy-policy">
              Personvernerklæring
            </router-link>
          </div>
          <Onboarding />
        </div>

        <div class="flex flex-col gap-2">
          <h3 class="font-bold text-lg">Om oss</h3>
          <div>
            <router-link class="opacity-60 hover:opacity-100" to="/about-us"> Om oss</router-link>
          </div>
        </div>

        <!--
        <div class="flex flex-col gap-2">
          <h3 class="font-bold text-lg">Socials</h3>
          <div>
            <a class="opacity-60 hover:opacity-100" href="#"> Twitch </a>
          </div>

          <div>
            <a class="opacity-60 hover:opacity-100" href="#"> Discord </a>
          </div>

          <div>
            <a class="opacity-60 hover:opacity-100" href="#"> Dribbble </a>
          </div>
        </div>
        -->
      </div>

      <h3>2025 Developed by Group 06</h3>
    </div>
  </footer>
</template>
