import { describe, it, expect, vi } from 'vitest';
import Boat from '@/utils/boat';

describe('Boat Class', () => {
  // mert
  it('constructs with correct properties', () => {
    const boat = new Boat('id1', 3, 'board');
    expect(boat.id).toBe('id1');
    expect(boat.length).toBe(3);
    expect(boat.placed.value).toBeTruthy();
  });

  // mert
  it('addSquare adds a square to the squares array', () => {
    const boat = new Boat('id1', 3, 'board');
    const mockSquare = {};
    boat.addSquare(mockSquare);
    expect(boat.squares).toContain(mockSquare);
  });

  // mert
  it('rotate changes direction and updates position', () => {
    const boat = new Boat('id1', 3, 'board');

    boat.board = { placeBoatAtXY: vi.fn(() => true) };
    boat.allowPlacement = true;
    boat.placed = true;

    const initialDir = boat.dir;
    boat.rotate();

    expect(boat.dir).not.toBe(initialDir);

    expect(boat.board.placeBoatAtXY).toHaveBeenCalled();
  });

  // mert
  it('dragStart sets isBeingDragged and section', () => {
    const boat = new Boat('id1', 3, 'board');
    boat.allowPlacement = true;
    boat.placed = false;

    const mockElement = {
      getBoundingClientRect: () => ({ x: 50, y: 50, width: 300, height: 100 })
    };

    const mockEvent = {
      target: mockElement,
      x: 100,
      y: 100
    };

    boat.dragStart(mockEvent);

    expect(boat.isBeingDragged).toBeTruthy();
    expect(boat.section).toBeDefined();
  });

});