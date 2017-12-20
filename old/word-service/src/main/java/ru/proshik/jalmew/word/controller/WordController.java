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
import ru.proshik.jalmew.common.model.word.Ex;
import ru.proshik.jalmew.common.model.word.Tr;
import ru.proshik.jalmew.common.model.word.WordOut;
import ru.proshik.jalmew.common.model.word.WordShortOut;
import ru.proshik.jalmew.common.model.ytranslate.ExampleTransfer;
import ru.proshik.jalmew.common.model.ytranslate.YTranslateWordOut;
import ru.proshik.jalmew.word.client.YTranslateServiceClient;
import ru.proshik.jalmew.word.controller.exception.TranslateNotFoundException;
import ru.proshik.jalmew.word.controller.exception.WordNotFoundException;
import ru.proshik.jalmew.word.repository.WordRepository;
import ru.proshik.jalmew.word.repository.model.Example;
import ru.proshik.jalmew.word.repository.model.Translated;
import ru.proshik.jalmew.word.repository.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    @Transactional
//    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST, value = "word/{text}")
    public WordShortOut add(@PathVariable("text") String text, String theme, String section) {

        return toOutShort(wordRepository.searchByTest(text)
                .orElseGet(() -> saveWord(getTranslateWord(text), text, theme, section)));
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "word")
    public ResponseEntity search(@RequestParam(value = "text", required = false) String text,
                                 @RequestParam(value = "wordId", required = false) List<String> wordId) {

        return ResponseEntity.ok(toOut(wordRepository.search(
                text,
                wordId != null
                        ? wordId
                        : new ArrayList<>())));
    }

    @RequestMapping(value = "word/{wordId}")
    @PreAuthorize("#oauth2.hasScope('server')")
    public WordOut get(@PathVariable("wordId") String wordId) {

        return toOut(wordRepository.findOne(wordId)
                .orElseThrow(() -> new WordNotFoundException(wordId)));
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

    @ExceptionHandler(TranslateNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Translate not found")
    public void translateNotFountExceptionHandler(TranslateNotFoundException e) {
        log.error("Not exists translate word for text={}", e.getText());
    }

    @ExceptionHandler(WordNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Word not found")
    public void wordNotFountExceptionHandler(WordNotFoundException e) {
        log.error("Word not found with wordId={}", e.getWordId());
    }

    private YTranslateWordOut getTranslateWord(String text) {

        YTranslateWordOut translate = yTranslateServiceClient.translate(text);

        if (translate == null || translate.isEmptyResult())
            throw new TranslateNotFoundException(text);
        else
            return translate;
    }

    private Word saveWord(YTranslateWordOut yTranslateWordOut, String text, String theme, String section) {
        return wordRepository.save(transform(yTranslateWordOut, text, theme, section));
    }

    private Word transform(YTranslateWordOut translate, String sourceWord, String theme, String section) {

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
                                .map(ex -> new Example(ex.getText(), ex.getTr().stream()
                                        .findFirst()
                                        .orElseGet(ExampleTransfer::new)
                                        .getText()))
                                .collect(Collectors.toList())
                                : Collections.emptyList()))
                .collect(Collectors.toList()));
    }

    private WordOut toOut(Word word) {
        return new WordOut(
                word.getId(),
                word.getText(),
                word.getTheme(),
                word.getSection(),
                word.getTranslated().stream()
                        .map(tr -> new Tr(
                                tr.getText(),
                                tr.getTranslate(),
                                tr.getTrs(),
                                tr.getPartOfSpeech(),
                                tr.getGen(),
                                tr.getExamples().stream()
                                        .map(ex -> new Ex(
                                                ex.getText(),
                                                ex.getTranslate()))
                                        .collect(Collectors.toList())))
                        .collect(Collectors.toList()));
    }

    private List<WordOut> toOut(List<Word> word) {
        return word.stream()
                .map(this::toOut)
                .collect(Collectors.toList());
    }

    private WordShortOut toOutShort(Word word) {
        Translated translated = word.getTranslated().stream()
                .findFirst()
                .orElseGet(Translated::new);

        return new WordShortOut(word.getId(),
                word.getText(),
                translated.getTranslate(),
                translated.getTrs());
    }

    private List<WordShortOut> toOutShort(List<Word> word) {
        return word.stream()
                .map(this::toOutShort)
                .collect(Collectors.toList());
    }

}
