package ru.proshik.jalmew.common.model.wordbook;

/**
 * Created by proshik on 28.09.16.
 */
public class WordbookShortOut {

    public String wordId;

    public int progressPercent;

    public WordbookShortOut() {
    }

    public WordbookShortOut(String wordId, int progressPercent) {
        this.wordId = wordId;
        this.progressPercent = progressPercent;
    }

    public String getWordId() {
        return wordId;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

}
