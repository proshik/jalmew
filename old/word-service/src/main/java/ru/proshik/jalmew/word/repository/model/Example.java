package ru.proshik.jalmew.word.repository.model;

/**
 * Created by proshik on 09.08.16.
 * <p>
 * Inner Entity
 */
public class Example {

    private String text;

    private String translate;

    public Example(String text, String translate) {
        this.text = text;
        this.translate = translate;
    }

    public String getText() {
        return text;
    }

    public String getTranslate() {
        return translate;
    }
}
