package io.gith;

public enum Order {
    TILE(5),
    GUI(6);


    private final int value;

    Order(int value) {
        this.value = value;
    }

}
