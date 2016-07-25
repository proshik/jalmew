package ru.proshik.jalmew.auth.service;


import ru.proshik.jalmew.auth.controller.dto.UserDto;

public interface UserService {

    void create(UserDto user);

}
