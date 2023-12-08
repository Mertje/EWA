import { describe, it, expect, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import GameListItem from "@/components/activeGames/GameListItem.vue";
import { createRouter, createWebHistory } from 'vue-router';

const mockRoutes = [{ path: '/play/:gameId', name: 'play' }];
const mockRouter = createRouter({
  history: createWebHistory(),
  routes: mockRoutes,
});

vi.mock('@/router', () => ({
  default: {
    push: vi.fn(),
  },
}));


describe('GameListItem', () => {

  // Mert
  it('displays the title and gameId props correctly', () => {
    const title = 'Test Game';
    const gameId = 'game123';

    const wrapper = mount(GameListItem, {
      props: { title, gameId },
      global: {
        plugins: [mockRouter]
      }
    });

    expect(wrapper.text()).toContain(title);
  });

  // Mert
  it('has correct class and style bindings', () => {
    const wrapper = mount(GameListItem, {
      props: { title: 'Test Game', gameId: 'game123' },
      global: {
        plugins: [mockRouter]
      }
    });

    const container = wrapper.find('div');
    expect(container.classes()).toContain('flex');
    expect(container.classes()).toContain('items-center');
    expect(container.classes()).toContain('justify-between');
    expect(container.classes()).toContain('shadow-gameItem');
  });

  // Mert
  it('renders the image with the correct source', () => {
    const wrapper = mount(GameListItem, {
      props: { title: 'Test Game', gameId: 'game123' },
      global: {
        plugins: [mockRouter]
      }
    });

    const image = wrapper.find('img');
    expect(image.exists()).toBe(true);
    expect(image.attributes('src')).toContain('ship-1.jpg');
  });

});