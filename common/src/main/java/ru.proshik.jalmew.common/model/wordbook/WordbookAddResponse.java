package ru.proshik.jalmew.common.model.wordbook;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by proshik on 12.08.16.
 */
public class WordbookAddResponse {

    @NotBlank
    private String word;

    public WordbookAddResponse() {
    }

    public WordbookAddResponse(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
