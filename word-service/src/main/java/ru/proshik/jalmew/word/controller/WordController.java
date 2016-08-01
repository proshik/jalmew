package ru.proshik.jalmew.word.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by proshik on 28.07.16.
 */
@RestController
@RequestMapping(value = "/word")
public class WordController {

    @RequestMapping(value = "/available")
    public String available(Principal principal) {
        return "Available word-service for user: " + principal.getName();
    }

    @RequestMapping(path = "search", params = "{word}")
    public ResponseEntity searchByWord(@RequestParam("word") String word) {

        return ResponseEntity.ok("word");
    }

    @RequestMapping(value = "{wordId:\\d+}")
    public ResponseEntity getById(@PathVariable("wordId") String wordId) {

        return ResponseEntity.ok("1");
    }

    @RequestMapping(params = {"wordId"})
    public ResponseEntity getByIds(@RequestParam("wordId") List<String> wordId) {

        return ResponseEntity.ok(Arrays.asList("1", "2"));
    }

}
