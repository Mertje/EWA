import { createRouter, createWebHistory } from 'vue-router'
import AuthenticateView from "@/views/AuthenticateView.vue";
import HomeView from "@/views/HomeView.vue";
import GamesView from "@/views/GamesView.vue";

import LoginComponent from "@/components/authentication/UserLogin.vue";
import RegisterComponent from "@/components/authentication/UserRegister.vue";
import GameList from "@/components/activeGames/GameList.vue";
import GameContainer from "@/components/game/GameContainer.vue";

import { isAuthenticated, checkAuthentication } from '@/utils/auth';
import PlayerList from "@/components/players/PlayerList.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/auth',
      name: 'auth',
      component: AuthenticateView,
      redirect: '/auth/login',
      meta: { requiresAuth: false },
      children: [
        {
          path: 'login',
          component: LoginComponent
        },
        {
          path: 'register',
          component: RegisterComponent
        }
      ]
    },
    {
      path: '/',
      name: 'home',
      meta: { requiresAuth: false },
      component: HomeView
    },
    {
      path: '/games',
      name: 'games',
      component: GamesView,
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'lobby',
          component: GameList
        },
        {
          path: 'play/:gameId',
          name: 'play',
          component: GameContainer,
          props: true
        }
      ]
    },
    {
      path: "/players",
      name: 'players',
      component: PlayerList,
      meta: { requiresAuth: true }
    }

  ]
})

router.beforeEach((to) => {
  checkAuthentication();

  if (to.meta.requiresAuth && !isAuthenticated.value) {
    return { name: 'auth', query: { redirect: to.name } };
  }
});

export default router
