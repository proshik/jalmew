package ru.proshik.jalmew.common.model.word;

import java.util.List;
import java.util.Objects;

/**
 * Created by proshik on 26.09.16.
 */
public class WordOut {

    private String id;

    private String text;

    private String theme;

    private String section;

    private List<Tr> tr;

    public WordOut(String id) {
        this.id = id;
    }

    public WordOut(String id, String text, String theme, String section, List<Tr> tr) {
        this.id = id;
        this.text = text;
        this.theme = theme;
        this.section = section;
        this.tr = tr;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTheme() {
        return theme;
    }

    public String getSection() {
        return section;
    }

    public List<Tr> getTr() {
        return tr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordOut wordOut = (WordOut) o;
        return Objects.equals(id, wordOut.id) &&
                Objects.equals(text, wordOut.text) &&
                Objects.equals(theme, wordOut.theme) &&
                Objects.equals(section, wordOut.section) &&
                Objects.equals(tr, wordOut.tr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, theme, section, tr);
    }
}
