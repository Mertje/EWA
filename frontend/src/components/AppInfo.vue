<template>
    <main class="h-screen">
    <div class="container mx-auto">
      <div class="col">
        <h1 class="text-center text-7xl p-7 font-bold">Battleship</h1>
      </div>
      <div class="flex items-center justify-center">
        <div class="w-1/3 mr-6">
          <p class="font-bold">Introduction:</p>
          <p>
            This is a school project made for HVA 2<sup>nd</sup> year project enterprise web application.
            The front-end is made in Vue, and the backend in Java Spring Boot.
            In this web application you will be able to play the game Battleship against someone else,
            see your scores and have have fun.
          </p>
          <p class="pt-5 font-bold">This project was made by:</p>
          <ul>
            <li>Backend: Mert</li>
            <li>Frontend: Jarno</li>
          </ul>
        </div>
       <div>
         <img class="max-w-xl rounded-xl" src="@/assets/images/ship-1.jpg" alt="Warship on blue sea">
       </div>
      </div>
      <div class="flex items-center justify-center pt-10">
        <div>
          <img class="max-w-xl rounded-xl" src="@/assets/images/ship-1.jpg" alt="UML diagram">
        </div>
        <div class="w-1/3 mr-6 pl-4">
          Uml diagram
        </div>
      </div>
    </div>
  </main>
</template>
<script setup>
</script>