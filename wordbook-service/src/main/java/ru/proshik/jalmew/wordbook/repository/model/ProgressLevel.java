package ru.proshik.jalmew.wordbook.repository.model;

/**
 * Created by proshik on 28.07.16.
 * <p>
 * Level of learn word
 */
public enum ProgressLevel {

    P0(0),
    P_25(25),
    P_50(50),
    P_75(75),
    P_100(100);

    private int value;

    ProgressLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
