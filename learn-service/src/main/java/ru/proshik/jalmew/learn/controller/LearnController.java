package ru.proshik.jalmew.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.proshik.jalmew.common.model.learn.TrainingWordOut;
import ru.proshik.jalmew.common.model.learn.TrainingWordResultRequest;
import ru.proshik.jalmew.common.model.learn.WordForLearnOut;
import ru.proshik.jalmew.common.model.word.WordOut;
import ru.proshik.jalmew.common.model.wordbook.WordbookShortOut;
import ru.proshik.jalmew.learn.client.WordClient;
import ru.proshik.jalmew.learn.client.WordbookClient;

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

        List<WordbookShortOut> wordsOfUser = wordbookClient.listForLearn(principal.getName());

        if (wordsOfUser == null || wordsOfUser.isEmpty() || wordsOfUser.size() < 10) {
            return ResponseEntity.noContent().build();
        }

        List<WordbookShortOut> userWordsForLearn = wordsOfUser.stream()
                .filter(wlo -> wlo.getProgressPercent() < 100)
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected.stream();
                }))
                .limit(10)
                .collect(Collectors.toList());

        Map<String, WordbookShortOut> userWordsByWordId = userWordsForLearn.stream()
                .collect(Collectors.toMap(WordbookShortOut::getWordId, Function.identity()));

        List<WordOut> wordDetailsForLearn = wordClient.search(userWordsForLearn.stream()
                .map(WordbookShortOut::getWordId)
                .collect(Collectors.toSet()));

        if (wordDetailsForLearn == null || wordDetailsForLearn.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        List<WordForLearnOut> learnWords = wordDetailsForLearn.stream()
                .map(w -> new WordForLearnOut(
                        w.getId(),
                        w.getText(),
                        w.getTr().stream()
                                .findAny()
                                .orElseThrow(IllegalArgumentException::new)
                                .getTranslate(),
                        w.getTr().stream()
                                .findFirst()
                                .orElseThrow(IllegalArgumentException::new)
                                .getTrs(),
                        userWordsByWordId.get(w.getId()).getProgressPercent(),
                        buildSaltWords(w.getId(), wordDetailsForLearn)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new TrainingWordOut(learnWords));
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @RequestMapping(method = RequestMethod.PUT, value = "learn/training/words")
    public ResponseEntity trainingWordAnswer(@AuthenticationPrincipal Principal principal,
                                             @RequestBody List<TrainingWordResultRequest> result) {

        wordbookClient.saveStatistic(principal.getName(), result);

        return ResponseEntity.ok().build();
    }

    private List<String> buildSaltWords(String notIncludeWordId, List<WordOut> wordsForLearn) {
        return wordsForLearn.stream()
                .filter(wos -> !wos.getId().equals(notIncludeWordId))
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected.stream();
                }))
                .limit(5)
                .map(WordOut::getText)
                .collect(Collectors.toList());
    }

}
