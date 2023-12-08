<script setup>
import {onMounted, ref, watch} from 'vue';
  import { getData } from '@/utils/fetch';
  import PlayerListItem from "@/components/players/PlayerListItem.vue";

  const defaultUsers = ref([]);
  const page = ref(0);
  const filteredData = ref([]);
  const inputField = ref('');
  const errorMessage = ref('');

  onMounted(() => {
    getData(`accounts/${page.value}`).then(data => {
      filteredData.value = data;
      defaultUsers.value = data;
    });
  });

  watch(inputField, async () => {
    errorMessage.value = '';

    if (inputField.value.length === 0) {
      return filteredData.value = defaultUsers.value;
    }

    getData(`accounts/user/${inputField.value}`).then(data => {
      if(data.error) {
        errorMessage.value = data.message
        filteredData.value = defaultUsers.value;
        return;
      }
      filteredData.value = data;
    });
  });

</script>

<template>
  <div class="flex flex-col container mx-auto gap-4 items-center">
    <div class="flex items-center gap-2 max-w-md w-full my-8 p-2 border-2 border-blue-500 rounded-2xl bg-white">
      <input
          type="text"
          class="grow h-10 px-2 rounded-lg text-2xl"
          placeholder="Search for player"
          v-model="inputField"
      >
      <button class="w-10 aspect-square text-blue-500">
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="lucide lucide-search">
          <circle cx="11" cy="11" r="8"/>
          <path d="m21 21-4.3-4.3"/>
        </svg>
      </button>
    </div>
    <p class="text-red-700">{{errorMessage}}</p>
    <PlayerListItem
        v-for="user in filteredData"
        :key="user"
        :username="user"
    />
  </div>
</template>

<style scoped>
</style>