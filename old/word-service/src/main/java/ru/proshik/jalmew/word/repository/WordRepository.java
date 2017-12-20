package ru.proshik.jalmew.word.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.Repository;
import ru.proshik.jalmew.word.repository.model.Word;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by proshik on 09.08.16.
 */
@org.springframework.stereotype.Repository
public interface WordRepository extends Repository<Word, String> {

    Word save(Word entity);

    Optional<Word> findOne(String id);

    @Query("{'text': ?0}")
    Optional<Word> searchByTest(String text);

    @Query("{$and:[{$or:[{'text' : ?0}, {'id': { $in: ?1 } } ] } ] }")
    List<Word> search(String text, List<String> id);

}
