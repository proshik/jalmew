package ru.proshik.jalmew.word.controller.exception;

/**
 * Created by proshik on 02.10.16.
 */
public class WordNotFoundException extends RuntimeException {

    private final String wordId;

    public WordNotFoundException(String wordId) {
        this.wordId= wordId;
    }


    public String getWordId() {
        return wordId;
    }
}
