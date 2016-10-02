package ru.proshik.jalmew.common.model.ytranslate;

import java.util.List;

/**
 * Created by proshik on 22.05.16.
 */
public class YTranslateWordOut {

    private Head head;

    private List<Definition> def;

    public YTranslateWordOut() {
    }

    public Head getHead() {
        return head;
    }

    public List<Definition> getDef() {
        return def;
    }

    public boolean isEmptyResult(){
        return def.isEmpty();
    }
}
