import { describe, it, expect, vi } from 'vitest';
import { boardFactory } from '@/utils/board';
import Square from '@/utils/Square';
import Boat from '@/utils/boat';

describe('Board Factory', () => {

  // mert
  it('setupSquares populates boardSquares correctly', () => {
    const board = boardFactory('board1');
    board.setupSquaresTest(); // Assuming this sets up 100 squares
    expect(board.boardSquares.value.length).toBe(100);
    expect(board.boardSquares.value[0]).toBeInstanceOf(Square);
  });

  // mert
  it('getSquaresAsRows groups squares into rows correctly', () => {
    const board = boardFactory('board1');
    board.setupSquaresTest();

    const rows = board.getSquaresAsRows();
    expect(rows.length).toBe(10);
    rows.forEach(row => {
      expect(row.length).toBe(10);
    });
  });

  // mert
  it('checkIfSpaceIsFreeFromXY correctly determines if space is free', () => {
    const board = boardFactory('board1');
    board.setupSquaresTest(); // Set up squares
    const boat = new Boat(1, 3, board); // Create a new boat

    let isFree = board.checkIfSpaceIsFreeFromXY(boat, 0, 0);
    expect(isFree).toBeTruthy();
  });

});