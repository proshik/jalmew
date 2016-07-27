package ru.proshik.jalmew.wordbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.proshik.jalmew.wordbook.client.AuthServiceClient;
import ru.proshik.jalmew.wordbook.controller.dto.UserDto;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by proshik on 24.07.16.
 */
@RestController
@RequestMapping(value = "/wordbook")
public class WordbookController {

    @Autowired
    private AuthServiceClient authClient;

    @RequestMapping(value = "/available")
    public String available(Principal principal) {
        return "Available wordbook-service for user: " + principal.getName();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createNewAccount(@Valid @RequestBody UserDto user) {
        authClient.createUser(user);
        return ResponseEntity.ok(user);
    }

}
