import { describe, it, expect, vi } from 'vitest';
import Square from '@/utils/square';

describe('Square Class', () => {

  // mert
  it('constructs with correct properties', () => {
    const square = new Square('id1', 1, 1, 'board', false);
    expect(square.id).toBe('id1');
    expect(square.x).toBe(1);
    expect(square.y).toBe(1);
    expect(square.isEmpty.value).toBeTruthy();
    expect(square.attackedStatus.value).toBeTruthy();
  });

  // mert
  it('sets and gets element', () => {
    const square = new Square('id1', 1, 1, 'board', false);
    const mockElement = {
      classList: {
        add: vi.fn(), remove: vi.fn()
      }
    };

    square.setElement(mockElement);
    expect(square.getElement()).toBe(mockElement);
  });

  // mert
  it('resets data and updates DOM element', () => {
    const square = new Square('id1', 1, 1, 'board', false);

    const mockElement = {
      classList: { add: vi.fn(), remove: vi.fn() },
      removeAttribute: vi.fn()
    };
    square.setElement(mockElement);

    square.show = vi.fn();

    square.resetData();

    expect(square.isEmpty.value).toBeTruthy();
    expect(square.boat.value).toBeUndefined();

    expect(mockElement.removeAttribute).toHaveBeenCalledWith('rowspan');
    expect(mockElement.removeAttribute).toHaveBeenCalledWith('colspan');

    expect(square.show).toHaveBeenCalled();
  });


});