package ru.proshik.jalmew.word.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by proshik on 28.07.16.
 */
@RestController
@RequestMapping(value = "word")
public class WordController {

    @RequestMapping(value = "/available")
    public String available(Principal principal) {
        return "Available word-service for user: " + principal.getName();
    }

}
