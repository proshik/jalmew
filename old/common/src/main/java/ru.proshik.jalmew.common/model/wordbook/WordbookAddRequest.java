package ru.proshik.jalmew.common.model.wordbook;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by proshik on 12.08.16.
 */
public class WordbookAddRequest {

    @NotBlank
    private String text;

    public WordbookAddRequest() {
    }

    public WordbookAddRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
