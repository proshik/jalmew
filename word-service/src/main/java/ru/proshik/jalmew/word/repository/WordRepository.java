package ru.proshik.jalmew.word.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.proshik.jalmew.word.model.Word;

import java.util.List;

/**
 * Created by proshik on 09.08.16.
 */
@Repository
public interface WordRepository extends MongoRepository<Word, String> {

    Word save(Word entity);

    Word findOne(String id);

    List<Word> findByIdIn(List<String> id);

    @Query("{'text' : ?0}")
    Word searchByText(String text);

}
