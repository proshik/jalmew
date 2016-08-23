package ru.proshik.jalmew.learn.controller.dto;

import java.util.List;

/**
 * Created by proshik on 21.08.16.
 */
public class LearnTranslateWord {

    private List<LearnWord> words;
    private String hashLearnSession;

    public LearnTranslateWord() {
    }

    public LearnTranslateWord(List<LearnWord> words) {
        this.words = words;
    }

    public LearnTranslateWord(List<LearnWord> words, String hashLearnSession) {
        this.words = words;
        this.hashLearnSession = hashLearnSession;
    }

    public List<LearnWord> getWords() {
        return words;
    }

    public String getHashLearnSession() {
        return hashLearnSession;
    }
}
