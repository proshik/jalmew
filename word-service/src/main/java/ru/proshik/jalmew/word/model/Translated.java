package ru.proshik.jalmew.word.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by proshik on 09.08.16.
 */
public class Translated {

    private String text;

    private String translate;

    private String trs;

    private String partOfSpeech;

    private String gen;

    private List<Example> examples = new ArrayList<>();

    public Translated(String text, String trs, String translate, String partOfSpeech, String gen, List<Example> examples) {
        this.text = text;
        this.trs = trs;
        this.translate = translate;
        this.partOfSpeech = partOfSpeech;
        this.gen = gen;
        this.examples = examples;
    }

    public String getText() {
        return text;
    }

    public String getTranslate() {
        return translate;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getTrs() {
        return trs;
    }

    public String getGen() {
        return gen;
    }

    public List<Example> getExamples() {
        return examples;
    }
}
