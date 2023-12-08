<script setup>
  import { onMounted, ref } from 'vue';
  import Game from '@/components/activeGames/GameListItem.vue';
  import { getData } from '@/utils/fetch';

  const games = ref([]);

  onMounted(() => {
    getData('games').then(data => {
      games.value = data;
    });
  });
</script>

<template>
  <div class="flex flex-col container mx-auto gap-4 items-center my-8 p-2">
    <Game v-for="game in games" :key="game.id" :title="game.userNames.join(' vs ')" :gameId="game.id"/>
    <div v-if="games.length === 0">
      <p class="text-2xl text-center mb-4">No games found</p>
      <router-link to="/players">
        <button class="py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 text-center rounded-lg">
          Goto player list to start a new game
        </button>
      </router-link>
    </div>
  </div>
</template>

<style scoped>

</style>