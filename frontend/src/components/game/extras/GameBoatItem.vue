<script setup>
  import { ref } from 'vue';

  const props = defineProps({
    boat: Object,
    isSetup: Boolean
  });

  const classRotated = ref('rotate(0deg)');

  const rotate = (event) => {
    if (!props.isSetup) {
      return;
    }
    props.boat.rotate();
    classRotated.value = props.boat.dir == 1 ? `rotate(-90deg) translateX(-${props.boat.size}px)` : 'rotate(0deg)';
  }
</script>

<template>
  <div
    :id="'boat-'+ boat.id"
    :style="{ width: boat.getWH().width + 'px', height: boat.getWH().height + 'px' }"
    :draggable="isSetup ? boat.allowPlacement : false"
    @dragstart="boat.dragStart"
    @click="rotate"
  />
</template>

<style scoped>
  div {
    @apply top-[-1px] left-[-1px] cursor-move origin-top-left;
    /* overflow: hidden; */
    transform: v-bind(classRotated)
  }

  div::before {
    content: "";
    @apply bg-[url('@/assets/images/battleship-1.png')]
    bg-[length:100%_100%]
    bg-no-repeat bg-center absolute
    w-full h-full;
  }
</style>