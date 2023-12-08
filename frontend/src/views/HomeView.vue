<script setup>
  import { isAuthenticated } from '@/utils/auth.js';
  import AppInfo from '../components/AppInfo.vue';
  import LobbyView from './LobbyView.vue';
</script>

<template>
  <LobbyView v-if="isAuthenticated"/>
  <AppInfo v-else/>
</template>
