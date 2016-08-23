package ru.proshik.jalmew.wordbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.proshik.jalmew.wordbook.model.Wordbook;

import java.util.List;

/**
 * Created by proshik on 30.07.16.
 */
@Repository
public interface WordbookRepository extends JpaRepository<Wordbook, Long> {

    Wordbook findByUsernameAndWordId(String username, String wordId);

    @Query("select w.wordId from Wordbook w where w.username = :username")
    List<String> findWordIdByUsername(@Param("username") String username);

    @Query("select w from Wordbook w where w.username = :username")
    List<Wordbook> findWordbooksByUsername(@Param("username") String username);
}
