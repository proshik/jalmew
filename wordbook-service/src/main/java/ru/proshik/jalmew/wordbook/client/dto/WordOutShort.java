package ru.proshik.jalmew.wordbook.client.dto;

/**
 * Created by proshik on 18.08.16.
 */
public class WordOutShort {

    private String id;

    private String text;

    private String translate;

    private String trs;

    public WordOutShort() {
    }

    public WordOutShort(String id, String text, String translate, String trs) {
        this.id = id;
        this.text = text;
        this.translate = translate;
        this.trs = trs;
    }

    public String getId() {
        return id;
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

}
