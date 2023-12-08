<script setup>
</script>

<template>
  <div class="flex flex-col container mx-auto gap-4 items-center">
    <div class="flex w-full p-16 justify-around">
      <div class="flex flex-col gap-8 justify-center font-semibold">
        <router-link to="/games" class="flex items-center justify-center py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 w-52 h-14 rounded-lg">
          <h2>Join battle</h2>
        </router-link>
        <router-link to="/players" class="flex items-center justify-center py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 w-52 h-14 rounded-lg">
          <h2>Show players</h2>
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
  td {
    @apply text-center
  }
</style>