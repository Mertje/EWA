import GameList from "@/components/activeGames/GameList.vue";
import { describe, it, expect, vi } from 'vitest';
import { mount, flushPromises } from '@vue/test-utils';

vi.mock('@/utils/fetch', () => ({
  getData: vi.fn(() => Promise.resolve([{ id: 'game1', userNames: ['Alice', 'Bob'] }, { id: 'game2', userNames: ['Charlie', 'Dana'] }])),
}));

describe('GameList', () => {
  // Mert
  it('displays no games message when there are no games', async () => {
    vi.mock('@/utils/fetch', () => ({
      getData: vi.fn(() => Promise.resolve([])),
    }));

    const wrapper = mount(GameList);
    await flushPromises();

    expect(wrapper.text()).toContain('No games found');
  });
});