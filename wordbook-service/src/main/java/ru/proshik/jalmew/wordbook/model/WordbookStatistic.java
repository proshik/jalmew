package ru.proshik.jalmew.wordbook.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.security.access.method.P;
import ru.proshik.jalmew.wordbook.model.enums.ProgressLevel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by proshik on 28.07.16.
 */
@Entity
@Table(name = "wordbook_statistic")
public class WordbookStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wordbook_statistic_seq")
    @GenericGenerator(name = "wordbook_statistic_seq", strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "wordbook_statistic_seq"),
                    @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1")})
    private Long id;

    @Column(name = "created_date", updatable = false, insertable = false, nullable = false)
    private Date createdDate;

    @Column(name = "progress_level", nullable = false)
    private ProgressLevel progressLevel = ProgressLevel.P0;

    public WordbookStatistic() {
    }

    public void incrementValue() {
        switch (getProgressLevel()) {
            case P0:
                setProgressLevel(ProgressLevel.P_25);
                break;
            case P_25:
                setProgressLevel(ProgressLevel.P_50);
                break;
            case P_50:
                setProgressLevel(ProgressLevel.P_75);
                break;
            case P_75:
                setProgressLevel(ProgressLevel.P_100);
                break;
            case P_100:
                break;
            default:
                break;
        }
    }


    public Long getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public ProgressLevel getProgressLevel() {
        return progressLevel;
    }

    public void setProgressLevel(ProgressLevel progressLevel) {
        this.progressLevel = progressLevel;
    }
}
