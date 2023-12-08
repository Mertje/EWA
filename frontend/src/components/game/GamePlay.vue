<script>
  import { boardFactory } from "@/utils/board";
  import BoardItem from "./extras/GameBoard.vue";
  import BoatItem from "./extras/GameBoatItem.vue";
  import PopupComponent from "@/components/PopupComponent.vue";
  import { getData, putData } from "@/utils/fetch";

  export default {
    data() {
      return {
        playerBoard: null,
        enemyBoard: null,
        boats: [],
        watchSquare: null,
        status: null,
        showPopup: false,
        selectedSquareId: null,
      }
    },
    props: ['boardData'],
    emits: ['updateGame'],
    inject: ['socket'],
    mounted() {
      this.playerBoard = boardFactory(this.boardData.userBoard.id);
      this.playerBoard.setupSquares(this.boardData.userBoard.squares);
      this.playerBoard.setupBoats(this.boardData.userBoard.ships);
      this.boats = this.playerBoard.boardBoats;
      this.watchSquare = this.playerBoard.boardSquares[99]

      this.enemyBoard = boardFactory(this.boardData.oppositeBoard.id);
      this.enemyBoard.setupSquares(this.boardData.oppositeBoard.attackedSquares);
      this.isPlayerTurn = this.boardData.playerTurn === this.boardData.userBoard.accountId;
      this.statusChecker();

      this.socket.subscribe(`/topic/game/${this.boardData.gameID}`, message => {
        console.log('Received: ', message.body);
        this.$emit('updateGame');
        this.$forceUpdate();
        return;
        // TODO update the boards instead of updating the whole game.
        const data = JSON.parse(message.body);
        const gameId = data.gameId;
        const attackedSquareId = data.attackedSquareId;
        if (gameId !== this.boardData.gameID) return;
        const square = this.playerBoard.boardSquares.find(square => square.id === attackedSquareId);
        if (!square) return;
      });
    },
    watch: {
      'watchSquare.element'() {
        for (const boat of this.boats) {
          const firstSquare = boat.squares[0];
          boat.squares = [];
          this.playerBoard.placeBoatAtXY(boat, firstSquare.x, firstSquare.y)
        }
      }
    },
    methods: {
      surrenderClick() {
        this.showPopup = true;
      },
      async surrenderConfirm() {
        const res = await getData(`games/${this.boardData.gameID}/surrender`);
        if (res) {
          this.status = res.message;
          this.showPopup = false;
          setTimeout(() => {
            this.$router.push({ name: "home" });
          }, 2000);
        }
      },
      cancel() {
        this.showPopup = false;
      },
      selectedBox(id) {
        this.selectedSquareId = id
      },
      async postAttack() {
        const res = await putData(`games/${this.boardData.gameID}/attack`, { attackedSquareId: this.selectedSquareId });
        const data = await res.text();
        if(data.toLowerCase() === "already hit") {
          this.status = "You already hit this square";
        } else if (data.toLowerCase() === "miss") {
          this.status = "You missed";
        } else if (data.toLowerCase() === "hit") {
          this.status = "You hit a boat";
        } else if (data.toLowerCase().startsWith("Game over")) {
          this.status = data;
          setTimeout(() => {
            this.$router.push({ name: "home" });
          }, 10000);
        } else if (data.toLowerCase().startsWith("Destroyed ship")) {
          this.status = data;
        } else {
          this.status = "Something went wrong";
        }

        this.socket.send(`/app/attack/${this.boardData.gameID}`, JSON.stringify({ attackedSquareId: this.selectedSquareId }), {});
      },
      statusChecker() {
        const gameCall = this.boardData;

        if (!gameCall.oppositeBoard.shipsPlaced) {
          this.status = "The opposite player hasn't placed his boats yet"
        }
        else if (gameCall.playerTurn !== gameCall.userBoard.accountId) {
          this.status = "Its not your turn yet"
        } else {
          this.status = "It's your turn"
        }
      },
    },
    components: {
      PopupComponent,
      BoardItem,
      BoatItem
    }
  }
</script>

<template>
  <div class="items-center h-full">
    <div class="flex items-center justify-between flex-row portrait:flex-col w-full">
      <BoardItem :board="playerBoard" :selectable="false" />
      <BoardItem
        :board="enemyBoard"
        :selectable="isPlayerTurn"
        @getSelectedSquareID="selectedBox"
      />
    </div>
    <div class="flex flex-col ">
      <p class="text-center pt-5">{{ status }}</p>
      <button
        class="mx-auto px-5 bg-amber-400 py-2 mt-5 text-white"
        @click="postAttack"
      >Attack</button>
    </div>
    <div class="flex flex-col">
      <button class="mx-auto px-5 bg-red-600 text-white py-2 mt-5" @click="surrenderClick">Surrender</button>
    </div>
  </div>
  <PopupComponent
    :confirm="surrenderConfirm"
    :cancel="cancel"
    :show-dialog="showPopup"
    text="Are you sure you want to surrender?"
  />
  <div class="hidden">
    <BoatItem
      v-for="boat in boats"
      :key="boat.id"
      :boat="boat"
      :isSetup="false"
      class="absolute"
    />
  </div>
</template>

<style scoped>
</style>