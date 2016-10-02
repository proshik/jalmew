package ru.proshik.jalmew.common.model.wordbook;

import ru.proshik.jalmew.common.model.word.WordOut;

/**
 * Created by proshik on 22.08.16.
 */
public class WordbookOut {

    private WordOut wordOut;

    public int progressPercent;

    public WordbookOut() {
    }

    public WordbookOut(WordOut wordOut, int progressPercent) {
        this.wordOut = wordOut;
        this.progressPercent = progressPercent;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public WordOut getWordOut() {
        return wordOut;
    }
}
