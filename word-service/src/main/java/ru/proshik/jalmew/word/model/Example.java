package ru.proshik.jalmew.word.model;

/**
 * Created by proshik on 09.08.16.
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
