package ru.proshik.jalmew.wordbook.model.y_dict;

/**
 * Created by proshik on 22.05.16.
 */
public class Example {

    private String text;

    private ExampleTransfer[] tr;

    public Example() {
    }

    public String getText() {
        return text;
    }

    public ExampleTransfer[] getTr() {
        return tr;
    }
}
