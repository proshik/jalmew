package ru.proshik.jalmew.word.client.ytranslate_dto;

import java.util.List;

/**
 * Created by proshik on 22.05.16.
 */
public class Definition {

    private String text;

    private String pos;

    private String ts;

    private List<Transfer> tr;

    public Definition() {
    }

    public String getText() {
        return text;
    }

    public String getPos() {
        return pos;
    }

    public String getTs() {
        return ts;
    }

    public List<Transfer> getTr() {
        return tr;
    }
}
