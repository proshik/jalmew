package ru.proshik.jalmew.common.model.learn;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by proshik on 09.09.16.
 */
public class TrainingWordResultRequest {

    @NotBlank
    private String wordId;
    @NotNull
    private boolean result;

    public TrainingWordResultRequest() {
    }

    public TrainingWordResultRequest(String wordId, boolean result) {
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
