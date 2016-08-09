package ru.proshik.jalmew.word.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by proshik on 09.08.16.
 */
public class Translated {

    private String text;

    private String gen;

    private List<Example> examples = new ArrayList<>();

    public Translated(String text, String gen, List<Example> examples) {
        this.text = text;
        this.gen = gen;
        this.examples = examples;
    }

    public String getText() {
        return text;
    }

    public String getGen() {
        return gen;
    }

    public List<Example> getExamples() {
        return examples;
    }
}
