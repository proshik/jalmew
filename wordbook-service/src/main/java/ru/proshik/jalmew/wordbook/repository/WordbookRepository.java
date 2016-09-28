package ru.proshik.jalmew.wordbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.proshik.jalmew.wordbook.repository.model.Wordbook;

import java.util.List;

/**
 * Created by proshik on 30.07.16.
 */
@Repository
public interface WordbookRepository extends JpaRepository<Wordbook, Long>, JpaSpecificationExecutor<Wordbook> {

    Wordbook findByUsernameAndWordId(String username, String wordId);

    List<Wordbook> findByUsernameAndWordIdIn(String username, List<String> wordIds);

    // TODO: 29.09.16 delete comment after check it is working
//    @Query("select w.wordId from Wordbook w where w.username = :username")
    List<String> findWordIdByUsername(@Param("username") String username);

    // TODO: 29.09.16 delete comment after check it is working
//    @Query("select w from Wordbook w where w.username = :username")
    List<Wordbook> findByUsername(@Param("username") String username);
}
