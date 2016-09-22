package ru.proshik.jalmew.word.controller;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.proshik.jalmew.common.dto.ytranslate.model.YTranslateWord;
import ru.proshik.jalmew.word.client.YTranslateServiceClient;
import ru.proshik.jalmew.word.controller.dto.WordOutShort;
import ru.proshik.jalmew.word.model.Example;
import ru.proshik.jalmew.word.model.Translated;
import ru.proshik.jalmew.word.model.Word;
import ru.proshik.jalmew.word.repository.WordRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by proshik on 28.07.16.
 */
@RestController
@RequestMapping(value = "/api/v1.0")
public class WordController {

    private static final Logger log = LoggerFactory.getLogger(WordController.class);

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private YTranslateServiceClient yTranslateServiceClient;

    @ExceptionHandler


    @Transactional
    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST, value = "word/{text}")
    public ResponseEntity add(@PathVariable("text") String text, String theme, String section) {

        Word foundWord = wordRepository.searchByText(text);

        if (foundWord == null) {
            YTranslateWord translate = yTranslateServiceClient.translate(text);
            if (translate.getDef().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            foundWord = wordRepository.save(transform(translate, text, theme, section));
        }
        return ResponseEntity.ok(toOut(foundWord));
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "word")
    public ResponseEntity search(@RequestParam(value = "text", required = false) String text,
                                 @RequestParam(value = "wordId", required = false) Set<String> wordId) {

        Word foundWord = wordRepository.searchByText(text);

        if (foundWord == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toOut(wordRepository.searchByText(text)));
    }

    @RequestMapping(value = "word/{wordId}")
    @PreAuthorize("#oauth2.hasScope('server')")
    public ResponseEntity get(@PathVariable("wordId") String wordId) {

        Word word = wordRepository.findOne(wordId);

        if (word == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toOut(word));
    }

    @RequestMapping(value = "word/search")
    public ResponseEntity getByIds(@RequestParam("wordId") Set<String> wordIds) {

        return ResponseEntity.ok(toOut(wordRepository.findByIdIn(wordIds)));
    }

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

    private Word transform(YTranslateWord translate, String sourceWord, String theme, String section) {

        return new Word(sourceWord, theme, section, translate.getDef().stream()
                .flatMap(definition ->
                        definition.getTr().stream()
                                .map(tr -> new ImmutableTriple<>(definition.getText(), definition.getTs(), tr)))
                .map(v -> new Translated(
                        v.getLeft(),
                        v.getMiddle(),
                        v.getRight().getText(),
                        v.getRight().getPos() != null
                                ? v.getRight().getPos()
                                : null,
                        v.getRight().getGen() != null
                                ? v.getRight().getGen()
                                : null,
                        v.getRight().getEx() != null
                                ? v.getRight().getEx().stream()
                                .map(ex -> new Example(ex.getText(), ex.getText()))
                                .collect(Collectors.toList())
                                : Collections.emptyList()))
                .collect(Collectors.toList()));
    }

    private WordOutShort toOut(Word word) {
        Translated translated = word.getTranslated().stream()
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        return new WordOutShort(word.getId(), word.getText(), translated.getTranslate(), translated.getTrs());
    }

    private List<WordOutShort> toOut(List<Word> words) {

        return words.stream()
                .map(this::toOut)
                .collect(Collectors.toList());
    }

}
