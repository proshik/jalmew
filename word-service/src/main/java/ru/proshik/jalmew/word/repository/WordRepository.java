package ru.proshik.jalmew.word.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.proshik.jalmew.word.model.Word;

import java.util.List;

/**
 * Created by proshik on 09.08.16.
 */
@Repository
public interface WordRepository extends CrudRepository<Word, String> {

    @Query("{'text' : ?0}")
    List<Word> searchByText(String text);



}
