package ru.proshik.jalmew.wordbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.proshik.jalmew.common.model.learn.TrainingWordResultRequest;
import ru.proshik.jalmew.common.model.word.WordOut;
import ru.proshik.jalmew.common.model.word.WordShortOut;
import ru.proshik.jalmew.common.model.wordbook.WordbookAddResponse;
import ru.proshik.jalmew.common.model.wordbook.WordbookOut;
import ru.proshik.jalmew.common.model.wordbook.WordbookShortOut;
import ru.proshik.jalmew.wordbook.client.AuthServiceClient;
import ru.proshik.jalmew.wordbook.client.WordClient;
import ru.proshik.jalmew.wordbook.controller.model.*;
import ru.proshik.jalmew.wordbook.repository.model.Wordbook;
import ru.proshik.jalmew.wordbook.repository.model.ProgressLevel;
import ru.proshik.jalmew.wordbook.repository.WordbookRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    private WordClient wordClient;

    @Autowired
    private WordbookRepository wordbookRepository;

    @RequestMapping(value = "account/user", method = RequestMethod.POST)
    public void createNewAccount(@Valid @RequestBody UserRequest user) {
        authClient.createUser(user);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "wordbook")
    public ResponseEntity addWord(Principal principal, @RequestBody @Valid WordbookAddResponse word) {

        WordShortOut foundWord = wordClient.add(word.getWord());

        // TODO: 12.08.16 добавить тут обработку разных статусо ответа( может быть 404 - не найдено). Логично вынести это в сервис
        if (foundWord == null) {
            return ResponseEntity.notFound().build();
        }

        Wordbook wordFromWordbook = wordbookRepository.findByUsernameAndWordId(principal.getName(), foundWord.getId());

        // TODO: 12.08.16 вообще тут лучше бы сделать upsert и транзакции не надо, понять как это сделать через Hibernate
        if (wordFromWordbook != null) {
            return ResponseEntity.ok(foundWord);
        }

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

        WordOut word = wordClient.getById(wordId);

        // TODO: 12.08.16 добавить тут обработку разных статусо ответа( может быть 404 - не найдено). Логично вынести это в сервис
        if (word == null) {
            return ResponseEntity.badRequest().build();
        }

        wordbookRepository.save(new Wordbook(principal.getName(), word.getId()));

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "wordbook/{wordId}")
    public ResponseEntity getWord(Principal principal, @PathVariable("wordId") String wordId) {

        Wordbook wordFromWordbook = wordbookRepository.findByUsernameAndWordId(principal.getName(), wordId);

        if (wordFromWordbook == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new WordbookOut(
                wordClient.getById(wordId),
                wordFromWordbook.getStatistic().getProgressLevel().getValue()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "wordbook")
    public ResponseEntity getWords(@AuthenticationPrincipal Principal principal,
                                   @PageableDefault Pageable pageable) {

        List<Wordbook> wordbookUser = wordbookRepository.findByUsername(principal.getName());

        if (wordbookUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, WordOut> words = wordClient.searchByIds(
                wordbookUser.stream()
                        .map(Wordbook::getWordId)
                        .collect(Collectors.toSet())).stream()
                .collect(Collectors.toMap(WordOut::getId, Function.identity()));

        return ResponseEntity.ok(wordbookUser.stream()
                .map(wb -> new WordbookOut(words.get(wb.getWordId()), wb.getStatistic().getProgressLevel().getValue()))
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
                                        @RequestParam(value = "progressLevel", required = false) ProgressLevel level) {

        List<Wordbook> wordIdsByByUser = wordbookRepository.findByUsername(userName);

        if (wordIdsByByUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(wordIdsByByUser.stream()
                .map(wb -> new WordbookShortOut(wb.getWordId(), wb.getStatistic().getProgressLevel().getValue()))
                .collect(Collectors.toList()));
    }

    @Transactional
    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST, value = "wordbook/training/words/statistic/{userName}")
    public ResponseEntity trainingWordsStatistic(@PathVariable("userName") String userName,
                                                 @RequestBody List<TrainingWordResultRequest> result) {

        List<Wordbook> wordbooks = wordbookRepository.findByUsernameAndWordIdIn(
                userName,
                result.stream()
                        .map(TrainingWordResultRequest::getWordId)
                        .collect(Collectors.toList()));

        Map<String, Boolean> resultByWordId = result.stream()
                .collect(Collectors.toMap(TrainingWordResultRequest::getWordId, TrainingWordResultRequest::isResult));

        wordbooks.stream()
                .filter(wordbook -> resultByWordId.get(wordbook.getWordId()))
                .forEach(wordbook -> wordbook.getStatistic().incrementValue());

        wordbookRepository.save(wordbooks);

        return ResponseEntity.ok().build();
    }

}
