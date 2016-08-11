package ru.proshik.jalmew.wordbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.proshik.jalmew.wordbook.client.AuthServiceClient;
import ru.proshik.jalmew.wordbook.client.WordServiceClient;
import ru.proshik.jalmew.wordbook.controller.dto.UserDto;
import ru.proshik.jalmew.wordbook.model.Wordbook;
import ru.proshik.jalmew.wordbook.repository.WordbookRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;

/**
 * Created by proshik on 24.07.16.
 */
@RestController
@RequestMapping(value = "/wordbook")
public class WordbookController {

    @Autowired
    private AuthServiceClient authClient;

    @Autowired
    private WordServiceClient wordClient;

    @Autowired
    private WordbookRepository wordbookRepository;

    @RequestMapping(value = "/available")
    public String available(Principal principal) {
        return "Available wordbook-service for user: " + principal.getName();
    }

    @RequestMapping(value = "account/user", method = RequestMethod.POST)
    public void createNewAccount(@Valid @RequestBody UserDto user) {
        authClient.createUser(user);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "word/{wordId}")
    public ResponseEntity add(Principal principal, @PathVariable("wordId") String wordId) {

        Wordbook wordFromWordbook = wordbookRepository.findByUsernameAndWordId(principal.getName(), wordId);

        if (wordFromWordbook != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).build();
        }

        String word = wordClient.getById(wordId);
        if (word == null) {
            return ResponseEntity.badRequest().build();
        }

        wordbookRepository.save(new Wordbook(principal.getName(), wordId));

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "word/{wordId}")
    public ResponseEntity get(Principal principal, @PathVariable("wordId") String wordId) {

        Wordbook userFromWordBook = wordbookRepository.findByUsernameAndWordId(principal.getName(), wordId);

        if (userFromWordBook == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(wordClient.getById(wordId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "word")
    public ResponseEntity list(Principal principal) {
        List<String> wordIdsByByUser = wordbookRepository.findWordIdByUsername(principal.getName());

        if (wordIdsByByUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(wordClient.getByIds(new HashSet<>(wordIdsByByUser)));
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE, value = "word/{wordId}")
    public ResponseEntity delete(Principal principal, @PathVariable("wordId") String wordId) {

        Wordbook wordFromWordbook = wordbookRepository.findByUsernameAndWordId(principal.getName(), wordId);

        if (wordFromWordbook == null) {
            return ResponseEntity.badRequest().build();
        }

        wordbookRepository.delete(wordFromWordbook);

        return ResponseEntity.ok().build();
    }

}
