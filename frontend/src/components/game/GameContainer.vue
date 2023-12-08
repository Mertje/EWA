<script>
  import { getData, putData } from '@/utils/fetch';
  import GamePlaceBoats from './GamePlaceBoats.vue';
  import GamePlay from "./GamePlay.vue";
  import Websocket from "@/utils/websocket"

  export default {
    data() {
      return {
        allData: null,
        shipsPlaced: false,
        componentKey: 0,
        socket: new Websocket().connect()
      }
    },
    props: ['gameId'],
    provide() {
      return {
        socket: this.socket
      }
    },
    async mounted() {
      await this.getGameData()
      this.shipsPlaced = this.allData.userBoard.ships.every(ship => ship.placed);
    },
    beforeUnmount() {
      this.socket.disconnect();
    },
    methods: {
      async boatsPlaced() {
        await this.getGameData();
        this.shipsPlaced = this.allData.userBoard.ships.every(ship => ship.placed);
      },
      async getGameData() {
        const data = await getData(`games/${this.gameId}`);
        this.allData = data
        console.log(this.allData)
        this.componentKey += 1;
        return data;
      }
    },
    components: {
      GamePlaceBoats,
      GamePlay
    }
  }
</script>

<template>
  <div class="flex items-center h-full">
    <div class="flex items-center justify-between flex-row portrait:flex-col w-full">
      <GamePlaceBoats
        v-if="!shipsPlaced"
        @boatsPlaced="boatsPlaced"
        :boardData="allData"
      />
    <GamePlay v-else :boardData="allData" @update-game="getGameData" :key="componentKey"/>
    </div>
  </div>
</template>

<style scoped>

</style>