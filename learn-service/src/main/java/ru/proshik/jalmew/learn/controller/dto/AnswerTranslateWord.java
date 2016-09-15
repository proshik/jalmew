package ru.proshik.jalmew.learn.controller.dto;

/**
 * Created by proshik on 09.09.16.
 */
public class AnswerTranslateWord {

    private String wordId;
    private boolean result;

    public AnswerTranslateWord() {
    }

    public AnswerTranslateWord(String wordId, boolean result) {
        this.wordId = wordId;
        this.result = result;
    }

    public String getWordId() {
        return wordId;
    }

    public boolean isResult() {
        return result;
    }
}
