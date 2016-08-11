package ru.proshik.jalmew.word.client.y_translate_dto;

import java.util.List;

/**
 * Created by proshik on 22.05.16.
 */
public class YTranslateWord {

    private Head head;

    private List<Definition> def;

    public YTranslateWord() {
    }

    public Head getHead() {
        return head;
    }

    public List<Definition> getDef() {
        return def;
    }
}
