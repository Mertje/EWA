<script setup>
  import { postData } from "@/utils/fetch";
  import { ref } from "vue";
  import { authUser } from "@/utils/auth";

  const registerError = ref("")

  async function register (formData) {
    const formsValues = new FormData(formData.target);

    if (formsValues.get("password") !== formsValues.get("passwordCheck")) {
      registerError.value = "Passwords don't match";
      return;
    }

    formsValues.delete("passwordCheck")

    const request =  await postData("auth/register", formsValues, false);

    if (!request.token) {
      registerError.value = request.message;
      return;
    }

    authUser(request);
  }
</script>

<template>
  <p class="text-center pb-4 font-bold text-2xl">Register</p>
  <form @submit.prevent="register" class="flex flex-col gap-4">
    <div>
      <label for="email" class="block text-gray-700 font-bold mb-2">Email</label>
      <input type="email" id="email" name="email" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:border-blue-500" placeholder="Enter your email">
    </div>
    <div>
      <label for="username" class="block text-gray-700 font-bold mb-2">Username</label>
      <input type="username" id="username" name="username" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:border-blue-500" placeholder="Enter a username">
    </div>
    <div class="flex flex-col">
      <label for="password" class="block text-gray-700 font-bold mb-2">Password</label>
      <input type="password" id="password" name="password" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:border-blue-500" placeholder="Enter your password">
      <input type="password" id="passwordCheck" name="passwordCheck" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:border-blue-500 mt-3" placeholder="Enter password again">
    </div>
    <div class="flex flex-col">
      <label for="fileInput">Enter a profile picture (Not required):</label>
      <input type="file" id="fileInput" name="file" accept="image/*">
    </div>
    <p class="text-center text-red-700">
      {{ registerError }}
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