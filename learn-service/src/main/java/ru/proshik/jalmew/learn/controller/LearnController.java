package ru.proshik.jalmew.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.proshik.jalmew.learn.client.WordClient;
import ru.proshik.jalmew.learn.client.WordbookClient;
import ru.proshik.jalmew.learn.client.dto.WordListOut;
import ru.proshik.jalmew.learn.client.dto.WordOutShort;
import ru.proshik.jalmew.learn.controller.dto.LearnTranslateWord;
import ru.proshik.jalmew.learn.controller.dto.LearnWord;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by proshik on 20.08.16.
 */
@RestController
@RequestMapping("learn")
public class LearnController {

    @Autowired
    private WordbookClient wordbookClient;

    @Autowired
    private WordClient wordClient;

    @RequestMapping(method = RequestMethod.GET, value = "training/words")
    public ResponseEntity wordTranslate() {

        List<WordListOut> wordsOfUser = wordbookClient.list();

        if (wordsOfUser == null || wordsOfUser.isEmpty() || wordsOfUser.size() < 10) {
            return ResponseEntity.noContent().build();
        }

        List<WordListOut> userWordsForLearn = wordsOfUser.stream()
                .filter(wlo -> wlo.getProgressPercent() < 100)
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected.stream();
                }))
                .limit(10)
                .collect(Collectors.toList());

        Map<String, WordListOut> userWordsByWordId = userWordsForLearn.stream()
                .collect(Collectors.toMap(WordListOut::getWordId, Function.identity()));

        List<WordOutShort> wordDetailsForLearn = wordClient.getByIds(userWordsForLearn.stream()
                .map(WordListOut::getWordId)
                .collect(Collectors.toSet()));

        if (wordDetailsForLearn == null || wordDetailsForLearn.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

//        Set<String> saltWordsFromUserWords = wordsOfUser.stream()
//                .filter(wou ->
//                        wou.getProgressPercent() < 100 && !userWordsForLearn.stream()
//                                .map(WordListOut::getWordId).collect(Collectors.toList()).contains(wou.getWordId()))
//                .map(WordListOut::getWordId)
//                .collect(Collectors.toSet());
//
//        List<WordOutShort> saltWords = new ArrayList<>();
//
//        if (saltWordsFromUserWords.size() >= wordsOfUser.size() * 5){
//            saltWords = wordClient.getByIds(saltWordsFromUserWords);
//        } else {
//            saltWords = wordClient.getRandomWords(wordsOfUser.size() * 5);
//        }

        List<LearnWord> learnWords = wordDetailsForLearn.stream()
                .map(w -> new LearnWord(
                        w.getId(),
                        w.getText(),
                        w.getTranslate(),
                        w.getTrs(),
                        userWordsByWordId.get(w.getId()).getProgressPercent(),
                        buildSaltWords(w.getId(), wordDetailsForLearn)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new LearnTranslateWord(learnWords));
    }

    private List<String> buildSaltWords(String notIncludeWordId, List<WordOutShort> wordsForLeanr) {
        return wordsForLeanr.stream()
                .filter(wos -> !wos.getId().equals(notIncludeWordId))
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected.stream();
                }))
                .limit(5)
                .map(WordOutShort::getText)
                .collect(Collectors.toList());
    }

}
