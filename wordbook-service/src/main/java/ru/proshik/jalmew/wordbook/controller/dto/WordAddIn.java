package ru.proshik.jalmew.wordbook.controller.dto;

/**
 * Created by proshik on 12.08.16.
 */
public class WordAddIn {

    private String word;

    public WordAddIn() {
    }

    public WordAddIn(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
