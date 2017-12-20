package ru.proshik.jalmew.common.model.learn;

import java.util.List;

/**
 * Created by proshik on 21.08.16.
 */
public class TrainingWordOut {

    private List<WordForLearnOut> words;
    private String hashLearnSession;

    public TrainingWordOut() {
    }

    public TrainingWordOut(List<WordForLearnOut> words) {
        this.words = words;
    }

    public TrainingWordOut(List<WordForLearnOut> words, String hashLearnSession) {
        this.words = words;
        this.hashLearnSession = hashLearnSession;
    }

    public List<WordForLearnOut> getWords() {
        return words;
    }

    public String getHashLearnSession() {
        return hashLearnSession;
    }
}
