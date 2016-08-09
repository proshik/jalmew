package ru.proshik.jalmew.word.client.y_translate_dto;

/**
 * Created by proshik on 22.05.16.
 */
public class Transfer {

    private String text;

    private String pos;

    private String gen;

    private Synonyms[] syn;

    private Meaning[] mean;

    private Example[] ex;

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

    public Synonyms[] getSyn() {
        return syn;
    }

    public Meaning[] getMean() {
        return mean;
    }

    public Example[] getEx() {
        return ex;
    }
}
