package ru.proshik.jalmew.wordbook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.proshik.jalmew.common.model.learn.TrainingWordResultRequest;
import ru.proshik.jalmew.common.model.word.WordOut;
import ru.proshik.jalmew.common.model.word.WordShortOut;
import ru.proshik.jalmew.common.model.wordbook.WordbookAddRequest;
import ru.proshik.jalmew.common.model.wordbook.WordbookOut;
import ru.proshik.jalmew.common.model.wordbook.WordbookShortOut;
import ru.proshik.jalmew.wordbook.client.WordClient;
import ru.proshik.jalmew.wordbook.repository.WordbookRepository;
import ru.proshik.jalmew.wordbook.repository.model.ProgressLevel;
import ru.proshik.jalmew.wordbook.repository.model.Wordbook;

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

    private static final Logger log = LoggerFactory.getLogger(WordbookController.class);

    @Autowired
    private WordClient wordClient;

    @Autowired
    private WordbookRepository wordbookRepository;

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "wordbook")
    public ResponseEntity addWord(Principal principal, @RequestBody @Valid WordbookAddRequest body) {

        WordShortOut word = wordClient.add(body.getText());
        if (word == null) {
            return ResponseEntity.notFound().build();
        }

        // TODO: 12.08.16 phase II. Change on implements throw upsert with native query
        Wordbook wordFromWordbook = wordbookRepository.findByUsernameAndWordId(principal.getName(), word.getId());
        if (wordFromWordbook != null) {
            return ResponseEntity.ok(word);
        }

        wordbookRepository.save(new Wordbook(principal.getName(), word.getId()));

        return ResponseEntity.ok().build();
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "wordbook/{wordId}")
    public ResponseEntity addWord(Principal principal, @PathVariable("wordId") String wordId) {

        Wordbook wordFromWordbook = wordbookRepository.findByUsernameAndWordId(principal.getName(), wordId);

        if (wordFromWordbook != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        WordOut word = wordClient.getById(wordId);

        if (word == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

        WordOut word = wordClient.getById(wordId);

        if (word == null) {
            log.error("Error, not found word in word-service, but must will been");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(new WordbookOut(word, wordFromWordbook.getStatistic().getProgressLevel().getValue()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "wordbook")
    public ResponseEntity getWordbook(@AuthenticationPrincipal Principal principal,
                                      @PageableDefault Pageable pageable) {

        List<Wordbook> wordbookUser = wordbookRepository.findByUsername(principal.getName(), pageable);

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
    public ResponseEntity deleteWord(Principal principal, @PathVariable("wordId") String wordId) {

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

        // TODO: 02.10.16 phase II. Expansion API
        List<Wordbook> wordIdsByByUser = wordbookRepository.findByUsername(userName);

        return ResponseEntity.ok(wordIdsByByUser.stream()
                .map(wb -> new WordbookShortOut(wb.getWordId(), wb.getStatistic().getProgressLevel().getValue()))
                .collect(Collectors.toList()));
    }

    @Transactional
    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST, value = "wordbook/training/words/statistic/{userName}")
    public ResponseEntity trainingWordsStatistic(@PathVariable("userName") String userName,
                                                 @RequestBody @Valid List<TrainingWordResultRequest> result) {

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

    /********* Exception Handlers *********/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid parameters or content from body of request")
    public void validationExceptionHandler() {
        log.error("Validation request parameters/body content");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Required request body is missing")
    public void errorOnReadRequestExceptionHandler(HttpMessageNotReadableException e) {
        log.error("Error on read body from request", e);

    }

}
