import Boat from "./boat";
import Square from "./square";
import { ref } from "vue";

export const squareWidth = ref(0);

export function boardFactory(boardId) {
    const boardID = ref(boardId);
    const boardEL = ref(null);
    const boardSquares = ref([]);
    const boardBoats = ref([]);

    function setupSquares (squares) {
        for (let i = 0; i < squares.length; i++) {
            const squareObj = squares[i];
            const x = i % 10;
            const y = Math.floor(i / 10);
            const square = new Square(squareObj.id, x, y, this);
            square.setAttacked(squareObj.attacked);
            square.setHit(squareObj.attacked && squareObj.shipID != null);
            boardSquares.value.push(square);
        }
    }

    function setupSquaresTest () {
        for (let i = 0; i < 100; i++) {
            const x = i % 10;
            const y = Math.floor(i / 10);
            const square = new Square(i, x, y, this);
            boardSquares.value.push(square);
        }
    }

    function setupBoats (boats) {
        boats.forEach(boat => {
            const newBoat = new Boat(boat.id, boat.length, this);
            boardBoats.value.push(newBoat);
            if(!boat.placed) return;
            newBoat.addSquare(getSquareFromId(boat.squares[0].id))
        });
    }

    function setupBoatsTest () {
        boardBoats.value.push(new Boat(1, 2, this));
        boardBoats.value.push(new Boat(2, 3, this));
        boardBoats.value.push(new Boat(3, 3, this));
        boardBoats.value.push(new Boat(4, 4, this));
        boardBoats.value.push(new Boat(5, 5, this));
    }

    function getSquaresAsRows () {
        const rows = [];
        let row = [];
        for (const square of boardSquares.value) {
            if (square.x == 0 && square.y != 0) {
                rows.push(row);
                row = [];
            }
            row.push(square);
        }
        rows.push(row);
        return rows;
    }

    function getBoardElement () {
        return boardEL.value;
    }

    function setBoardElement (boardRef) {
        boardEL.value = boardRef;
    }

    function getSquareFromXY (x, y) {
        return boardSquares.value.find(square => square.x == x && square.y == y);
    }

    function getSquareFromId (id) {
        return boardSquares.value.find(square => square.id == id);
    }

    function getBoat (id) {
        return boardBoats.value.find(boat => boat.id == id);
    }

    function getBoatOnSquare (square) {
        return boardBoats.value.find(boat => boat.x == square.x && boat.y == square.y);
    }

    function isDropAllowed (event) {
        const boat = boardBoats.value.find(boat => boat.isBeingDragged);
        if (event.target.id.startsWith("boat")) return;
        const x = Number(event.target.dataset.x);
        const y = Number(event.target.dataset.y);
        const isFree = checkIfSpaceIsFreeFromXY(boat, x, y);
        if (isFree) {
            event.preventDefault();
        }
    }

    function dropBoat (event) {
        event.preventDefault();
        const boat = boardBoats.value.find(boat => boat.isBeingDragged);
        if (event.target.id.startsWith("boat")) return;
        const x = Number(event.target.dataset.x);
        const y = Number(event.target.dataset.y);
        boat.setPos(x, y);
    }

    function checkIfSpaceIsFreeFromXY (boat, squareX, squareY) {
        const { length, dir, section } = boat;

        // Check if the boat is out of bounds
        if (dir == 1 && (squareX - section < 0 || squareX + (length - section) > 10)) return false;
        if (dir == 2 && (squareY - section < 0 || squareY + (length - section) > 10)) return false;

        // Check if there is a boat in the way
        for (let i = 0; i < length; i++) {
            const checkX = dir == 1 ? (squareX - section) + i : squareX;
            const checkY = dir == 2 ? (squareY - section) + i : squareY;
            const checkSquare = getSquareFromXY(checkX, checkY);
            if (!checkSquare.isEmpty && checkSquare.boat != boat) return false;
        }
        return true;
    }

    function placeBoatAtXY (boat, squareX, squareY) {
        if(!checkIfSpaceIsFreeFromXY(boat, squareX, squareY)) return false;

        boardSquares.value.filter(square => square.boat == boat).forEach(square => square.resetData());

        const square = getSquareFromXY(squareX, squareY);
        const squareEL = square.getElement();
        const boatEL = boat.getElement();
        squareEL.appendChild(boatEL);

        boat.placed = true;
        if (boat.length == 1) return true;

        if (boat.dir == 1) {
            squareEL.setAttribute("colspan", boat.length);
        } else {
            squareEL.setAttribute("rowspan", boat.length);
        }

        for (let i = 0; i < boat.length; i++) {;
            let x = boat.dir == 1 ? square.x + i : square.x;
            let y = boat.dir == 1 ? square.y : square.y + i;

            const checkSquare = getSquareFromXY(x, y);
            checkSquare.setBoat(boat);
            if (checkSquare == square) continue;
            checkSquare.hide();
        }

        return true;
    }

    return {
        boardID,
        boardEL,
        boardSquares,
        boardBoats,
        squareWidth,
        setupBoats,
        setupBoatsTest,
        setupSquares,
        setupSquaresTest,
        getBoardElement,
        setBoardElement,
        getSquaresAsRows,
        getSquareFromXY,
        getSquareFromId,
        getBoat,
        getBoatOnSquare,
        isDropAllowed,
        dropBoat,
        checkIfSpaceIsFreeFromXY,
        placeBoatAtXY
    }

}