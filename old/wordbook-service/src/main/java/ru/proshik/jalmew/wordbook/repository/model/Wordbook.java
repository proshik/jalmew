package ru.proshik.jalmew.wordbook.repository.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by proshik on 28.07.16.
 * <p>
 * Entity
 */
@Entity
@Table(name = "wordbook")
public class Wordbook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wordbook_seq")
    @GenericGenerator(name = "wordbook_seq", strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = SequenceStyleGenerator.SEQUENCE_PARAM,
                            value = "wordbook_seq"),
                    @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1")})
    private Long id;

    @Column(name = "created_date", updatable = false, insertable = false, nullable = false)
    private Date createdDate;

    @Column(name = "username", updatable = false, nullable = false)
    private String username;

    @Column(name = "word_id", updatable = false, nullable = false)
    private String wordId;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "wordbook_statistic_id")
    private WordbookStatistic statistic = new WordbookStatistic();

    public Wordbook() {
    }

    public Wordbook(String username, String wordId) {
        this.username = username;
        this.wordId = wordId;
        this.statistic = statistic;
    }

    public Long getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getUsername() {
        return username;
    }

    public String getWordId() {
        return wordId;
    }

    public WordbookStatistic getStatistic() {
        return statistic;
    }
}
