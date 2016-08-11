package ru.proshik.jalmew.word.controller;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.proshik.jalmew.word.client.YTranslateServiceClient;
import ru.proshik.jalmew.word.client.y_translate_dto.YTranslateWord;
import ru.proshik.jalmew.word.controller.dto.WordOutShort;
import ru.proshik.jalmew.word.model.Example;
import ru.proshik.jalmew.word.model.Translated;
import ru.proshik.jalmew.word.model.Word;
import ru.proshik.jalmew.word.repository.WordRepository;

import java.security.Principal;
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

    @RequestMapping(value = "/available")
    public String available(Principal principal) {
        return "Available word-service for user: " + principal.getName();
    }

    @RequestMapping(method = RequestMethod.POST, value = "words/{word}")
    public ResponseEntity add(@PathVariable("word") String word) {

        if (word == null) {
            return ResponseEntity.badRequest().build();
        }

        YTranslateWord translate = yTranslateServiceClient.translate(word);

        if (translate.getDef().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Word save = wordRepository.save(toSave(translate, word));

        return ResponseEntity.ok(save);
    }

    @RequestMapping(value = "words/search", params = {"wordId=null"})
    public ResponseEntity searchByWord(@RequestParam("word") String word,
                                       @RequestParam("wordId") List<String> wordId) {

        if (word == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Word> words = wordRepository.searchByText(word);

        if (words.isEmpty()) {
            YTranslateWord translate = yTranslateServiceClient.translate(word);
            if (translate.getDef().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Word savedWord = wordRepository.save(toSave(translate, word));

            return ResponseEntity.ok(toOutShort(savedWord));
        }

        return ResponseEntity.ok(toOutShort(words));
    }

    @RequestMapping(value = "words/{wordId}")
    public ResponseEntity getById(@PathVariable("wordId") String wordId) {

        if (wordId == null) {
            return ResponseEntity.badRequest().build();
        }

        Word word = wordRepository.findOne(wordId);

        if (word == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(toOutShort(word));
    }

    @RequestMapping(value = "words")
    public ResponseEntity getByIds(@RequestParam("wordId") List<String> wordIds) {

        List<Word> words = wordRepository.findAll(wordIds);

        if (words.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toOutShort(words));
    }

    private Word toSave(YTranslateWord translate, String sourceWord) {

        return new Word(sourceWord, translate.getDef().stream()
                .flatMap(definition -> definition.getTr().stream()
                        .map(tr -> new ImmutableTriple<>(definition.getText(), definition.getTs(), tr)))
                .map(v -> new Translated(
                        v.getLeft(),
                        v.getMiddle(),
                        v.getRight().getText(),
                        v.getRight().getPos(),
                        v.getRight().getGen(),
                        v.getRight().getEx().stream()
                                .map(ex -> new Example(ex.getText(), ex.getText()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList()));

        /**
         * Two various
         */
//        Definition def = translate.getDef().stream()
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException());
//
//        Definition def = translate.getDef().stream()
//                .findFirst()
//                .orElseThrow(IllegalArgumentException::new);
//
//        return new Word(sourceWord, def.getTs(), def.getTr().stream()
//                .map(tr -> new Translated(def.getText(), tr.getText(), def.getTs(), tr.getGen(), tr.getEx().stream()
//                        .map(ex -> new Example(ex.getText(), ex.getText()))
//                        .collect(Collectors.toList())))
//                .collect(Collectors.toList()));

        /**
         * Third various
         */
//        List<Word> collect = translate.getDef().stream()
//                .map(def -> new Word(sourceWord, def.getTr().stream()
//                        .map(tr -> new Translated(def.getText(), tr.getText(), def.getTs(), def.getPos(), tr.getGen(), tr.getEx().stream()
//                                .map(ex -> new Example(ex.getText(), ex.getText()))
//                                .collect(Collectors.toList())))
//                        .collect(Collectors.toList())))
//                .collect(Collectors.toList());
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
