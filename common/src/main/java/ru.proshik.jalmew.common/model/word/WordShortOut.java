package ru.proshik.jalmew.common.model.word;

import java.util.Objects;

/**
 * Created by proshik on 12.08.16.
 */
public class WordShortOut {

    private String id;

    private String text;

    private String translate;

    private String trs;

    private String theme;

    private String section;

    public WordShortOut(String id) {
        this.id = id;
    }

    public WordShortOut(String id, String text, String translate, String trs) {
        this.id = id;
        this.text = text;
        this.translate = translate;
        this.trs = trs;
    }

    public WordShortOut(String id, String text, String translate, String trs, String theme, String section) {
        this.id = id;
        this.text = text;
        this.translate = translate;
        this.trs = trs;
        this.theme = theme;
        this.section = section;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordShortOut that = (WordShortOut) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(text, that.text) &&
                Objects.equals(translate, that.translate) &&
                Objects.equals(trs, that.trs) &&
                Objects.equals(theme, that.theme) &&
                Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, translate, trs, theme, section);
    }
}
