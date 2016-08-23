package ru.proshik.jalmew.learn.client.dto;

/**
 * Created by proshik on 22.08.16.
 */
public class WordListOut {

    public String wordId;
    public int progressPercent;

    public WordListOut(String wordId, int progressPercent) {
        this.wordId = wordId;
        this.progressPercent = progressPercent;
    }

    public String getWordId() {
        return wordId;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }
}
