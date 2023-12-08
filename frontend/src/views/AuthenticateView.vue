<script>
  export default {
    data() {
     return {
       link: '',
       text: ''
     }
    },
    methods: {
      handleRoute() {
        if(this.$route.path === '/auth/login') {
          this.text = 'No account?';
          this.link = '/auth/register'
          return;
        }
        this.text = 'Already registered?';
        this.link = '/auth/login'
      }
    },
    created() {
      this.handleRoute();
    },
    updated() {
      this.handleRoute();
    }
  }
</script>

<template>
  <!-- <main> -->
  <main class="w-screen h-screen bg-blue-300 flex justify-center items-center">
    <div class="bg-white p-8 rounded-lg shadow-md w-96">
      <router-view />
      <router-link :to="link">
        <p class="text-center pt-3 text-blue-800 underline"> {{ text }} </p>
      </router-link>
      <router-link class="cursor-pointer" to="/">
        <p class="text-center pt-3 text-blue-800 underline"> Back to home</p>
      </router-link>
    </div>
  </main>
</template>