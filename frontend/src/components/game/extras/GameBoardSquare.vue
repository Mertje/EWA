<script setup>
  import { onMounted, ref } from 'vue';
  const props = defineProps({
    square: Object,
    select: Function,
  });

  const squareRef = ref(null);

  onMounted(() => {
    props.square.setElement(squareRef.value);
  });

</script>

<template>
  <td
    ref="squareRef"
    :id="'square-'+square.x+'-'+square.y"
    :data-y="square.y" :data-x="square.x"
    class="border border-black box-border relative"
    :class="square.attacked && square.hit ? 'bg-red-700/50' : square.attacked && !square.hit ? 'bg-blue-700/50' : ''"
    @drop="square.board.dropBoat"
    @dragover="square.board.isDropAllowed"
    @click="e => $emit('clicked', e.target)"
  />
</template>

<style scoped>
  div {
    @apply bg-black top-[-1px] left-[-1px] cursor-move;
  }
</style>