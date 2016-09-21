package ru.proshik.jalmew.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.proshik.jalmew.learn.client.WordClient;
import ru.proshik.jalmew.learn.client.WordbookClient;
import ru.proshik.jalmew.learn.client.dto.WordListOut;
import ru.proshik.jalmew.learn.client.dto.WordOutShort;
import ru.proshik.jalmew.learn.controller.dto.AnswerTranslateWord;
import ru.proshik.jalmew.learn.controller.dto.LearnTranslateWord;
import ru.proshik.jalmew.learn.controller.dto.LearnWord;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by proshik on 20.08.16.
 */
@RestController
@RequestMapping("/api/v1.0")
public class LearnController {

    @Autowired
    private WordbookClient wordbookClient;

    @Autowired
    private WordClient wordClient;

    @PreAuthorize("#oauth2.hasScope('ui')")
    @RequestMapping(method = RequestMethod.GET, value = "learn/training/words")
    public ResponseEntity trainingWordTranslate(@AuthenticationPrincipal Principal principal) {

        List<WordListOut> wordsOfUser = wordbookClient.listForLearn(principal.getName());

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

        List<WordOutShort> wordDetailsForLearn = wordClient.search(userWordsForLearn.stream()
                .map(WordListOut::getWordId)
                .collect(Collectors.toSet()));

        if (wordDetailsForLearn == null || wordDetailsForLearn.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

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

    @PreAuthorize("#oauth2.hasScope('ui')")
    @RequestMapping(method = RequestMethod.POST, value = "learn/training/words")
    public ResponseEntity trainingWordAnswer(@AuthenticationPrincipal Principal principal,
                                             @RequestBody List<AnswerTranslateWord> result) {

        wordbookClient.saveStatistic(principal.getName(), result);

        return ResponseEntity.ok().build();
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
