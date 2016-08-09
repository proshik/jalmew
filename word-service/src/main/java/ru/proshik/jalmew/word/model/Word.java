package ru.proshik.jalmew.word.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by proshik on 09.08.16.
 */
@Document(collection = "words")
public class Word {

    @Id
    private String id;

    private String text;

    private String partOfSpeech;

    private String trs;

    private List<Translated> translated = new ArrayList<>();

    @PersistenceConstructor
    public Word(String text, String partOfSpeech, String trs, List<Translated> translated) {
        this.text = text;
        this.partOfSpeech = partOfSpeech;
        this.trs = trs;
        this.translated = translated;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getTrs() {
        return trs;
    }

    public List<Translated> getTranslated() {
        return translated;
    }
}
