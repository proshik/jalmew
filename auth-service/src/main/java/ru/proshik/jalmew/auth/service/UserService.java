package ru.proshik.jalmew.auth.service;


import ru.proshik.jalmew.common.model.wordbook.RegistrationRequest;

public interface UserService {

    void create(RegistrationRequest user);

}
