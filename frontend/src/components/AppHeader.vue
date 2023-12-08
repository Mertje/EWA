<script setup>
  import { isAuthenticated, logoutUser } from '@/utils/auth.js';
  import { getLocalStorageItem } from "@/utils/localStorage";
  import {ref, watch} from "vue";
  import { BASEURL } from "@/utils/fetch";

  const userName = ref();
  const profilePictureURL = ref()

  watch(isAuthenticated, () => {
    userName.value = getLocalStorageItem('userName');
    profilePictureURL.value = getLocalStorageItem('profilePicture');
  })
</script>

<template>
  <nav class="w-100 bg-blue-500 h-16 text-right flex flex-row items-center px-4 justify-between">
      <div>
        <router-link to="/">
          <p class="py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 w-32 text-center rounded-lg">Home</p>
        </router-link>
      </div>
      <div v-if="isAuthenticated" class="inline-flex">
        <div class="inline-flex">
          <img class="w-16 h-16 rounded-full" :src="BASEURL + profilePictureURL" alt="profile_picture">
          <p class="mx-2 my-auto">{{ userName }}</p>
        </div>
        <div class="m-auto">
          <button @click="logoutUser" class="py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 w-32 text-center rounded-lg">Logout</button>
        </div>
      </div>
      <div v-else class="flex divide-x-2 divide-black">
        <router-link class="cursor-pointer" to="/auth/login">
          <p class="py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 w-32 text-center rounded-l-lg">Login</p>
        </router-link>
        <router-link to="/auth/register">
          <p class="py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 w-32 text-center rounded-r-lg">Register</p>
        </router-link>
      </div>
  </nav>
</template>