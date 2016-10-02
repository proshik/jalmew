package ru.proshik.jalmew.common.model.word;

import java.util.List;
import java.util.Objects;

/**
 * Created by proshik on 26.09.16.
 */
public class Tr {

    private String text;

    private String translate;

    private String trs;

    private String partOfSpeech;

    private String gen;

    private List<Ex> ex;

    public Tr() {
    }

    public Tr(String text, String translate, String trs, String partOfSpeech, String gen, List<Ex> ex) {
        this.text = text;
        this.translate = translate;
        this.trs = trs;
        this.partOfSpeech = partOfSpeech;
        this.gen = gen;
        this.ex = ex;
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

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getGen() {
        return gen;
    }

    public List<Ex> getEx() {
        return ex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tr tr = (Tr) o;
        return Objects.equals(text, tr.text) &&
                Objects.equals(translate, tr.translate) &&
                Objects.equals(trs, tr.trs) &&
                Objects.equals(partOfSpeech, tr.partOfSpeech) &&
                Objects.equals(gen, tr.gen) &&
                Objects.equals(ex, tr.ex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, translate, trs, partOfSpeech, gen, ex);
    }
}
