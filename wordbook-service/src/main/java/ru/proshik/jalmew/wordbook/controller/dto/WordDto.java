package ru.proshik.jalmew.wordbook.controller.dto;

/**
 * Created by proshik on 12.08.16.
 */
public class WordDto {

    private String word;

    public WordDto() {
    }

    public WordDto(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
