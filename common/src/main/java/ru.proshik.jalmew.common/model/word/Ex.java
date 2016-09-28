package ru.proshik.jalmew.common.model.word;

import java.util.Objects;

/**
 * Created by proshik on 26.09.16.
 */
public class Ex {

    private String text;

    private String translate;

    public Ex(String text, String translate) {
        this.text = text;
        this.translate = translate;
    }

    public String getText() {
        return text;
    }

    public String getTranslate() {
        return translate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ex ex = (Ex) o;
        return Objects.equals(text, ex.text) &&
                Objects.equals(translate, ex.translate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, translate);
    }
}
