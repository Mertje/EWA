import { ref } from "vue"

export default class Square {
    constructor(id, x, y, board) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.isEmpty = ref(true);
        this.boat = ref(undefined);
        this.board = board;
        this.element = ref(null);
        this.attacked = ref(false);
        this.hit = ref(false);
    }

    setElement(element) {
        this.element = element;
    }

    getElement() {
        return this.element;
    }

    getBoat() {
        return this.boat;
    }

    setBoat(boat) {
        this.isEmpty = ref(false);
        this.boat = ref(boat);
        boat.addSquare(this);
    }

    setAttacked(isAttacked) {
        this.attacked = ref(isAttacked);
    }

    getAttacked() {
        return this.attacked;
    }

    setHit(isHit) {
        this.hit = ref(isHit);
    }

    getHit() {
        return this.hit;
    }

    hide() {
        this.getElement().classList.add("hidden");
    }

    show() {
        this.getElement().classList.remove("hidden");
    }

    resetData() {
        this.isEmpty = ref(true);
        this.boat = ref(undefined);
        const squareEL = this.getElement();
        this.show();
        squareEL.removeAttribute("rowspan");
        squareEL.removeAttribute("colspan");
    }
}
