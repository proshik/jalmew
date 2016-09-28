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
import ru.proshik.jalmew.word.repository.model.Example;
import ru.proshik.jalmew.word.repository.model.Translated;
import ru.proshik.jalmew.word.repository.model.Word;
import ru.proshik.jalmew.word.repository.WordRepository;

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
    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST, value = "word/{text}")
    public ResponseEntity add(@PathVariable("text") String text, String theme, String section) {

        Word foundWord = wordRepository.searchByTest(text);

        if (foundWord == null) {
            YTranslateWordOut translate = yTranslateServiceClient.translate(text);
            if (translate.getDef().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            foundWord = wordRepository.save(transform(translate, text, theme, section));
        }
        return ResponseEntity.ok(toOutShort(foundWord));
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "word")
    public ResponseEntity search(@RequestParam(value = "text", required = false) String text,
                                 @RequestParam(value = "wordId", required = false) List<String> wordId) {

        List<Word> foundWord = wordRepository.search(text, wordId);

        if (foundWord == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toOut(foundWord));
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
