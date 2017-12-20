package ru.proshik.jalmew.wordbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.proshik.jalmew.common.model.wordbook.RegistrationRequest;
import ru.proshik.jalmew.wordbook.client.AuthServiceClient;

import javax.validation.Valid;

/**
 * Created by proshik on 02.10.16.
 */
@RestController
@RequestMapping(value = "/api/v1.0")
public class AccountController {

    @Autowired
    private AuthServiceClient authClient;

    @RequestMapping(value = "account/user", method = RequestMethod.POST)
    public void createNewAccount(@RequestBody @Valid RegistrationRequest user) {
        authClient.createUser(user);
    }

}
