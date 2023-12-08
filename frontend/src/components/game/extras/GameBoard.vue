<script setup>
  import { ref, watch } from 'vue';
  import Square from './GameBoardSquare.vue';
  import { useEventListener } from "@/utils/events.js";
  import { squareWidth } from '@/utils/board.js';

  const props = defineProps({
    board: Object,
    selectable: Boolean
  });

  const squares = ref([]);
  const boardRef = ref(null);
  const selectedBox = ref(null);

  const emit = defineEmits(['getSelectedSquareID']);

  useEventListener(window, "resize", () => {
    squareWidth.value = boardRef.value.offsetWidth / 10;
  });

  watch(() => props.board, (board) => {
    if (board) {
      board.setBoardElement(boardRef.value);
      squares.value = board.getSquaresAsRows();
    }
  }, { immediate: true });

  function selectBox(square) {
    if (!square.attacked && props.selectable) {

      if (selectedBox.value) {
        selectedBox.value.element.classList.remove("bg-gray-500")
      }

      selectedBox.value = square;
      square.element.classList.add("bg-gray-500")

      emit('getSelectedSquareID', square.id);
    }
  }

  defineExpose({
    boardRef,
    squares
  });

</script>

<template>
  <table class="border-collapse border-spacing-0 min-w-[20rem] min-h-[20rem] lg:min-w-[720px] lg:min-h-[720px]" ref="boardRef">
    <tbody>
    <tr v-for="rows in squares" :key="rows">
      <Square
        v-for="square in rows"
        :square="square"
        :key="square.x + '-' + square.y"
        @clicked="selectBox(square)"
      />
    </tr>
    </tbody>
  </table>
</template>

<style scoped>
td > div {
  position: absolute;
}
</style>