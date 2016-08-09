package ru.proshik.jalmew.word.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.proshik.jalmew.word.client.YTranslateServiceClient;
import ru.proshik.jalmew.word.client.y_translate_dto.YTranslateWord;
import ru.proshik.jalmew.word.model.Example;
import ru.proshik.jalmew.word.model.Translated;
import ru.proshik.jalmew.word.model.Word;
import ru.proshik.jalmew.word.repository.WordRepository;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

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

        YTranslateWord translate = yTranslateServiceClient.translate(word);

        if (translate == null) {
            return ResponseEntity.notFound().build();
        }

        Example ex = new Example("last word", "последнее слово");

        Translated tr = new Translated("слово", "ср", Collections.singletonList(ex));

        Word wordForSave = new Word(word, "noun", "wɜːd", Collections.singletonList(tr));

        Word save = wordRepository.save(wordForSave);

        return ResponseEntity.ok(save);
    }

    @RequestMapping(value = "words/search", params = {"wordId=null"})
    public ResponseEntity searchByWord(@RequestParam("word") String word, @RequestParam("wordId") List<String> wordId) {

        List<Word> words = wordRepository.searchByText(word);

        if (words.isEmpty()) {
            // TODO: 09.08.16 сходить в переводчик
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(words);
    }

    @RequestMapping(value = "words/{wordId}")
    public ResponseEntity getById(@PathVariable("wordId") String wordId) {

        Word word = wordRepository.findOne(wordId);

        if (word == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(word);
    }

    @RequestMapping(value = "words")
    public ResponseEntity getByIds(@RequestParam("wordId") List<String> wordIds) {

        Iterable<Word> words = wordRepository.findAll(wordIds);

        return ResponseEntity.ok(words);
    }

}
