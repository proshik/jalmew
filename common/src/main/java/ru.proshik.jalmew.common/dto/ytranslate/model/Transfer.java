package ru.proshik.jalmew.common.dto.ytranslate.model;

import java.util.List;

/**
 * Created by proshik on 22.05.16.
 */
public class Transfer {

    private String text;

    private String pos;

    private String gen;

    private List<Synonyms> syn;

    private List<Meaning> mean;

    private List<Example> ex;

    public Transfer() {
    }

    public String getText() {
        return text;
    }

    public String getPos() {
        return pos;
    }

    public String getGen() {
        return gen;
    }

    public List<Synonyms> getSyn() {
        return syn;
    }

    public List<Meaning> getMean() {
        return mean;
    }

    public List<Example> getEx() {
        return ex;
    }
}
