<script setup>
  const props = defineProps({
    showDialog: Boolean,
    confirm: Function,
    cancel: Function,
    text: String
  })
</script>


<template>
  <div v-if="showDialog" class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50">
    <div class="bg-white p-6 rounded-lg text-center">
      <p class="mb-4">{{ text }}</p>
      <button class="bg-blue-300 hover:text-blue-50 hover:bg-blue-700 px-4 py-2 rounded-lg mr-2" @click="confirm">Yes</button>
      <button class="bg-gray-300 hover:text-gray-50 hover:bg-gray-700 px-4 py-2 rounded-lg" @click="cancel">No</button>
    </div>
  </div>
</template>