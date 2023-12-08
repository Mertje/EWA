<script setup>
    import router from '@/router';

    const props = defineProps({
        title: String,
        gameId: String,
    })

    const joinGame = () => {
        router.push({ name: "play", params: { gameId: props.gameId }})
    }

</script>

<template>
    <div class="flex items-center justify-between gap-2 w-full h-16 px-2 rounded-lg bg-opacity-10 bg-black shadow-gameItem">
        <div class="flex items-center gap-2">
            <img class="w-16 h-16 rounded-full" src="../../assets/images/ship-1.jpg">
            <h2 class="text-2xl font-bold">{{ title }}</h2>
        </div>
        <div class="flex items-center">
            <button class="py-1 px-4 bg-blue-300 hover:text-blue-50 hover:bg-blue-700 w-32 text-center rounded-lg" @click="joinGame">Join</button>
        </div>
    </div>
</template>

<style scoped>
</style>