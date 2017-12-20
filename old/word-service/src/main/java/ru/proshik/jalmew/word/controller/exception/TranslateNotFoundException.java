package ru.proshik.jalmew.word.controller.exception;

/**
 * Created by proshik on 02.10.16.
 */
public class TranslateNotFoundException extends RuntimeException {

    private final String text;

    public TranslateNotFoundException(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
