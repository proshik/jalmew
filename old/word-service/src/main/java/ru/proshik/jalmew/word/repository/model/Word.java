package ru.proshik.jalmew.word.repository.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by proshik on 09.08.16.
 * <p>
 * Entity
 */
@Document(collection = "words")
public class Word {

    @Id
    private String id;

    /**
     * Значение в исходном языке
     */
    private String text;

    /**
     * Тема слова
     */
    private String theme;

    /**
     * Раздел (пр. Топ 1000, Топ 3000)
     */
    private String section;

    /**
     * Массив переводов
     */
    private List<Translated> translated = new ArrayList<>();

    @PersistenceConstructor
    public Word(String text, String theme, String section, List<Translated> translated) {
        this.text = text;
        this.translated = translated;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTheme() {
        return theme;
    }

    public String getSection() {
        return section;
    }

    public List<Translated> getTranslated() {
        return translated;
    }
}
