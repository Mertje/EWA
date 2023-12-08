<script>
  import BoatItem from './extras/GameBoatItem.vue';
  import BoardItem from './extras/GameBoard.vue';
  import { boardFactory } from '@/utils/board';
  import { putData } from "@/utils/fetch";

  export default {
    data() {
      return {
        playerBoard: null,
        boats: [],
        allBoatsPlaced: false
      }
    },
    props: ['boardData'],
    emits: ['boatsPlaced'],
    watch: {
      boardData(boardData) {
        const boardRef = this.$refs.boardRef.boardRef;
        this.playerBoard = boardFactory(boardData.userBoard.id);
        this.playerBoard.setupSquares(boardData.userBoard.squares);
        this.playerBoard.setupBoats(boardData.userBoard.ships);
        this.playerBoard.setBoardElement(boardRef);
        this.boats = this.playerBoard.boardBoats;
      },
      boats: {
        handler(boats) {
          this.allBoatsPlaced = boats.every(boat => boat.placed);
        },
        deep: true
      }
    },
    methods: {
      async placeBoats() {
        const boats = this.playerBoard.boardBoats.map(boat => {
          return {
            shipId: boat.id,
            placedSquares: boat.squares.map(square => square.id),
          }
        });

        const data = await putData(`games/${this.boardData.gameID}/ship`, boats);
        const results = await data.text();

        if (results === "success") {
          this.$emit('boatsPlaced');
        }
      }
    },

    components: {
      BoatItem,
      BoardItem
    }
  }
</script>

<template>
  <div class="flex items-center h-full">
    <div class="flex items-center justify-between flex-row portrait:flex-col w-full gap-10">
      <BoardItem ref="boardRef" :board="playerBoard"/>
      <div v-if="allBoatsPlaced">
        <button @click="placeBoats" class="py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 w-32 text-center rounded-lg">Ready</button>
      </div>
      <div v-else id="boats" class=" grow flex flex-col basis-full">
        <h2 class="text-2xl font-bold text-center">Place your boats</h2>
        <p class="text-center">Click on a boat to rotate it</p>
        <div class="flex gap-4 items-end justify-center py-10">
          <BoatItem
            v-for="boat in boats"
            :key="boat.id"
            :boat="boat"
            :isSetup="true"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
td > div {
  position: absolute;
}
</style>