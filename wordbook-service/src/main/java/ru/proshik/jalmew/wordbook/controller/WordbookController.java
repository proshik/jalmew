package ru.proshik.jalmew.wordbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.proshik.jalmew.wordbook.client.AuthServiceClient;
import ru.proshik.jalmew.wordbook.client.WordServiceClient;
import ru.proshik.jalmew.wordbook.client.dto.WordOutShort;
import ru.proshik.jalmew.wordbook.controller.dto.AnswerTranslateWord;
import ru.proshik.jalmew.wordbook.controller.dto.UserDto;
import ru.proshik.jalmew.wordbook.controller.dto.WordAddIn;
import ru.proshik.jalmew.wordbook.controller.dto.WordListOut;
import ru.proshik.jalmew.wordbook.model.Wordbook;
import ru.proshik.jalmew.wordbook.model.enums.ProgressLevel;
import ru.proshik.jalmew.wordbook.repository.WordbookRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by proshik on 24.07.16.
 */
@RestController
@RequestMapping(value = "/api/v1.0")
public class WordbookController {

    @Autowired
    private AuthServiceClient authClient;

    @Autowired
    private WordServiceClient wordClient;

    @Autowired
    private WordbookRepository wordbookRepository;

    @RequestMapping(value = "account/user", method = RequestMethod.POST)
    public void createNewAccount(@Valid @RequestBody UserDto user) {
        authClient.createUser(user);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "wordbook")
    public ResponseEntity addWord(Principal principal, @RequestBody WordAddIn word) {

        if (word == null || word.getWord() == null) {
            return ResponseEntity.badRequest().build();
        }

        WordOutShort foundWord = wordClient.searchByText(word.getWord());

        // TODO: 12.08.16 добавить тут обработку разных статусо ответа( может быть 404 - не найдено). Логично вынести это в сервис
        if (foundWord == null) {
            return ResponseEntity.notFound().build();
        }

        Wordbook wordFromWordbook = wordbookRepository.findByUsernameAndWordId(principal.getName(), foundWord.getId());

        // TODO: 12.08.16 вообще тут лучше бы сделать upsert и транзакции не надо, понять как это сделать через Hibernate
        if (wordFromWordbook != null) {
            return ResponseEntity.ok(foundWord);
        }

        // TODO: 22.09.16 убрать этот галимый get(0)
        wordbookRepository.save(new Wordbook(principal.getName(), foundWord.getId()));

        return ResponseEntity.ok(foundWord);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "wordbook/{wordId}")
    public ResponseEntity addWord(Principal principal, @PathVariable("wordId") String wordId) {

        Wordbook wordFromWordbook = wordbookRepository.findByUsernameAndWordId(principal.getName(), wordId);

        if (wordFromWordbook != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).build();
        }

        WordOutShort word = wordClient.getById(wordId);

        // TODO: 12.08.16 добавить тут обработку разных статусо ответа( может быть 404 - не найдено). Логично вынести это в сервис
        if (word == null) {
            return ResponseEntity.badRequest().build();
        }

        wordbookRepository.save(new Wordbook(principal.getName(), wordId));

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "wordbook/{wordId}")
    public ResponseEntity getWord(Principal principal, @PathVariable("wordId") String wordId) {

        Wordbook userFromWordBook = wordbookRepository.findByUsernameAndWordId(principal.getName(), wordId);

        if (userFromWordBook == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(wordClient.getById(wordId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "wordbook")
    public ResponseEntity getWords(Principal principal) {

        List<Wordbook> wordIdsByByUser = wordbookRepository.findWordbooksByUsername(principal.getName());

        if (wordIdsByByUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // TODO: 15.09.16 надо еще ходить в word-service за переводом слов
        return ResponseEntity.ok(wordIdsByByUser.stream()
                .map(wb -> new WordListOut(wb.getWordId(), wb.getStatistic().getProgressLevel().getValue()))
                .collect(Collectors.toList()));
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE, value = "wordbook/{wordId}")
    public ResponseEntity delete(Principal principal, @PathVariable("wordId") String wordId) {

        Wordbook wordFromWordbook = wordbookRepository.findByUsernameAndWordId(principal.getName(), wordId);

        if (wordFromWordbook == null) {
            return ResponseEntity.badRequest().build();
        }

        wordbookRepository.delete(wordFromWordbook);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.GET, value = "wordbook/training/words/{userName}")
    public ResponseEntity trainingWords(@PathVariable("userName") String userName,
                                        @RequestParam(value = "count", required = false) Integer count,
                                        @RequestParam(value = "progressLevel", required = false) ProgressLevel levee) {
        List<Wordbook> wordIdsByByUser = wordbookRepository.findWordbooksByUsername(userName);

        if (wordIdsByByUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(wordIdsByByUser.stream()
                .map(wb -> new WordListOut(wb.getWordId(), wb.getStatistic().getProgressLevel().getValue()))
                .collect(Collectors.toList()));
    }

    @Transactional
    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST, value = "wordbook/training/words/statistic/{userName}")
    public ResponseEntity trainingWordsStatistic(@PathVariable("userName") String userName,
                                                 @RequestBody List<AnswerTranslateWord> result) {

        List<Wordbook> wordbooks = wordbookRepository.findByUsernameAndWordIdIn(userName, getWordIds(result));

        Map<String, Boolean> resultByWordId = result.stream()
                .collect(Collectors.toMap(AnswerTranslateWord::getWordId, AnswerTranslateWord::isResult));

        wordbooks.stream()
                .filter(wordbook -> resultByWordId.get(wordbook.getWordId()))
                .forEach(wordbook -> wordbook.getStatistic().incrementValue());

        wordbookRepository.save(wordbooks);

        return ResponseEntity.ok().build();
    }

    private List<String> getWordIds(List<AnswerTranslateWord> result) {
        return result.stream()
                .map(AnswerTranslateWord::getWordId)
                .collect(Collectors.toList());
    }

}
