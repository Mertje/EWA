import { ref } from "vue";
import { squareWidth } from "./board";

export default class Boat {
    constructor(id, length, board) {
        this.id = id;
        this.length = length;
        this.size = squareWidth;
        this.dir = ref(2);
        this.section = ref(0);
        this.placed = ref(false);
        this.x = ref(-1);
        this.y = ref(-1);
        this.board = board;
        this.allowPlacement = ref(true);
        this.squares = [];
        this.isBeingDragged = ref(false);
    }

    getElement() {
        return document.getElementById(`boat-${this.id}`);
    }

    addSquare(square) {
        this.squares.push(square);
    }

    setPos(x, y) {
        if (!this.allowPlacement) return;
        this.x = x;
        this.y = y;

        this.placed = this.board.placeBoatAtXY(this, this.x, this.y);
        if (!this.placed) {
            this.x = -1;
            this.y = -1;
        }
        this.isBeingDragged = false;
    }

    getPos() {
        return {x: this.x, y: this.y};
    }

    getWH() {
        const width = this.size;
        const height = this.size * this.length;
        return {width, height};
    }

    rotate() {
        if (!this.allowPlacement) return;
        if (!this.placed) return;
        this.dir = this.dir == 1 ? 2 : 1;
        this.setPos(this.x, this.y);
        if (!this.placed) {
            this.dir = this.dir == 1 ? 2 : 1;
        }
    }

    disablePlacement() {
        this.allowPlacement = false;
    }

    dragStart(event) {
        if (!this.allowPlacement) return;
        if (this.placed) return;
        this.isBeingDragged = true;

        if(this.length == 1) return;

        const boat = event.target;
        const clientX = event.x;
        const clientY = event.y;
        const boatRect = boat.getBoundingClientRect();
        const relativeX = clientX - boatRect.x;
        const relativeY = clientY - boatRect.y;

        let section = 1;

        if (this.dir == 1) {
            section = Math.floor(relativeX / this.getWH().height);
        } else {
            section = Math.floor(relativeY / this.getWH().width);
        };

        this.section = section;
    }

}