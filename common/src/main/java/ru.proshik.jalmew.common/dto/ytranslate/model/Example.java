package ru.proshik.jalmew.common.dto.ytranslate.model;

import java.util.List;

/**
 * Created by proshik on 22.05.16.
 */
public class Example {

    private String text;

    private List<ExampleTransfer> tr;

    public Example() {
    }

    public String getText() {
        return text;
    }

    public List<ExampleTransfer> getTr() {
        return tr;
    }
}
