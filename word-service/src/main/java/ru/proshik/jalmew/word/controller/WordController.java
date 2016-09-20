package ru.proshik.jalmew.word.controller;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.proshik.jalmew.word.client.YTranslateServiceClient;
import ru.proshik.jalmew.word.client.ytranslate_dto.YTranslateWord;
import ru.proshik.jalmew.word.controller.dto.WordOutShort;
import ru.proshik.jalmew.word.model.Example;
import ru.proshik.jalmew.word.model.Translated;
import ru.proshik.jalmew.word.model.Word;
import ru.proshik.jalmew.word.repository.WordRepository;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by proshik on 28.07.16.
 */
@RestController
@RequestMapping(value = "/word")
public class WordController {

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private YTranslateServiceClient yTranslateServiceClient;

    @Transactional
//    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST, value = "words/{word}")
    public ResponseEntity add(@PathVariable("word") String word) {

        if (word == null) {
            return ResponseEntity.badRequest().build();
        }

        Word foundWord = wordRepository.searchByText(word);

        if (foundWord == null) {
            YTranslateWord translate = yTranslateServiceClient.translate(word);
            if (translate.getDef().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Word savedWord = wordRepository.save(toSave(translate, word));

            return ResponseEntity.ok(toOutShort(savedWord));
        } else {
            return ResponseEntity.ok(toOutShort(foundWord));
        }
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "words/search")
    public ResponseEntity search(@RequestParam("word") String word) {

        if (word == null) {
            return ResponseEntity.badRequest().build();
        }

        Word foundWord = wordRepository.searchByText(word);

        // TODO: 15.09.16 выпилить получение слов через yTranslate. Это должен быть поиск только по сохраненным словам
        if (foundWord == null) {
            YTranslateWord translate = yTranslateServiceClient.translate(word);
            if (translate.getDef().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Word savedWord = wordRepository.save(toSave(translate, word));

            return ResponseEntity.ok(toOutShort(savedWord));
        }

        return ResponseEntity.ok(toOutShort(foundWord));
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(value = "words/{wordId}")
    public ResponseEntity get(@PathVariable("wordId") String wordId) {

        if (wordId == null) {
            return ResponseEntity.badRequest().build();
        }

        Word word = wordRepository.findOne(wordId);

        if (word == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(toOutShort(word));
    }

    private Word toSave(YTranslateWord translate, String sourceWord) {

        return new Word(sourceWord, translate.getDef().stream()
                .flatMap(definition -> definition.getTr().stream()
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

        /**
         * Two various
         * ***********

         Definition def = translate.getDef().stream()
         .findFirst()
         .orElseThrow(() -> new IllegalArgumentException());

         Definition def = translate.getDef().stream()
         .findFirst()
         .orElseThrow(IllegalArgumentException::new);

         return new Word(sourceWord, def.getTs(), def.getTr().stream()
         .map(tr -> new Translated(def.getText(), tr.getText(), def.getTs(), tr.getGen(), tr.getEx().stream()
         .map(ex -> new Example(ex.getText(), ex.getText()))
         .collect(Collectors.toList())))
         .collect(Collectors.toList()));

         *
         */

        /**
         * Third various
         * *************

         List<Word> collect = translate.getDef().stream()
         .map(def -> new Word(sourceWord, def.getTr().stream()
         .map(tr -> new Translated(def.getText(), tr.getText(), def.getTs(), def.getPos(), tr.getGen(), tr.getEx().stream()
         .map(ex -> new Example(ex.getText(), ex.getText()))
         .collect(Collectors.toList())))
         .collect(Collectors.toList())))
         .collect(Collectors.toList());

         *
         */
    }

    private WordOutShort toOutShort(Word word) {
        Translated translated = word.getTranslated().stream()
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        return new WordOutShort(word.getId(), word.getText(), translated.getTranslate(), translated.getTrs());
    }

    private List<WordOutShort> toOutShort(List<Word> words) {

        return words.stream()
                .map(this::toOutShort)
                .collect(Collectors.toList());
    }

}
