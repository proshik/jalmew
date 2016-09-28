package ru.proshik.jalmew.auth.service;


import ru.proshik.jalmew.auth.controller.dto.UserRequest;

public interface UserService {

    void create(UserRequest user);

}
