<script setup>
  import router from '@/router';
  import { postData } from "@/utils/fetch";
  import { ref } from "vue";
  import PopupComponent from "@/components/PopupComponent.vue";

  const props = defineProps({
    username: String,
  })

  const showDialog = ref(false);
  let boardId;

  const createGame = async () => {
    const results = await postData('games', { username: props.username });
    const errorMessage = 'You already have a game with this person:';

    if (results.message && results.message.includes(errorMessage)) {
      boardId = results.message.split(':')[1]
      showDialog.value = true;
      return;
    }

    return await router.push({ name: "play", params: { gameId: results.id }});
  };

  const confirm = async () => {
    await router.push({ name: 'play', params: { gameId: boardId } });
    showDialog.value = false;
  };

  const cancel = () => {
    boardId = undefined;
    showDialog.value = false;
  };

</script>

<template>
  <div class="flex items-center justify-between gap-2 w-full h-16 px-2 rounded-lg bg-opacity-10 bg-black shadow-gameItem">
    <div class="flex items-center gap-2">
      <img class="w-16 h-16 rounded-full" src="@/assets/images/ship-1.jpg">
      <h2 class="text-2xl font-bold">{{ username }}</h2>
    </div>
    <div class="flex items-center">
      <button class="py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 w-32 text-center rounded-lg" @click="createGame">Create Game</button>
    </div>
  </div>
  <PopupComponent
    :confirm="confirm"
    :cancel="cancel"
    :show-dialog="showDialog"
    text="You already have a game with this person. Do you want to proceed?"
  />
</template>