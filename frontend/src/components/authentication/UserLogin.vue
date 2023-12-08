<script setup>
  import { postData } from "@/utils/fetch";
  import { ref } from "vue";
  import { authUser } from "@/utils/auth";

  const loginError = ref("")

  async function login (formData) {
    const formsValues =  new FormData(formData.target)

    const data = {
      "email" : formsValues.get("email"),
      "password": formsValues.get("password")
    }

    const request =  await postData("auth/login", data)

    if (request.error) {
      loginError.value = request.message;
    }

    authUser(request);
  }
</script>

<template>
  <p class="text-center pb-4 font-bold text-2xl">Login</p>
  <form @submit.prevent="login" class="flex flex-col gap-4">
    <div>
      <label for="email" class="block text-gray-700 font-bold mb-2">Email</label>
      <input required type="email" id="email" name="email" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:border-blue-500" placeholder="Enter your email">
    </div>
    <div class="flex flex-col">
      <label for="password" class="block text-gray-700 font-bold mb-2">Password</label>
      <input required type="password" id="password" name="password" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:border-blue-500" placeholder="Enter your password">
    </div>
    <p class="pb-3 text-center text-red-700">
      {{ loginError }}
    </p>
    <div class="text-center">
      <button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-full focus:outline-none focus:shadow-outline-blue active:bg-blue-800">Login</button>
    </div>
  </form>
</template>

<style scoped>
  p {
    text-wrap: balance;
  }
</style>