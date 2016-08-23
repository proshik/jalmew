package ru.proshik.jalmew.learn.controller.dto;

import java.util.List;

/**
 * Created by proshik on 21.08.16.
 */
public class LearnWord {

    private String wordId;
    private String text;
    private String translate;
    private String trs;
    private Integer progressPercent;
    private List<String> saltWords;

    public LearnWord() {
    }

    public LearnWord(String wordId, String text, String translate, String trs, Integer progressPercent,
                     List<String> saltWords) {
        this.wordId = wordId;
        this.text = text;
        this.translate = translate;
        this.trs = trs;
        this.progressPercent = progressPercent;
        this.saltWords = saltWords;
    }

    public String getWordId() {
        return wordId;
    }

    public String getText() {
        return text;
    }

    public String getTranslate() {
        return translate;
    }

    public String getTrs() {
        return trs;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public List<String> getSaltWords() {
        return saltWords;
    }
}
