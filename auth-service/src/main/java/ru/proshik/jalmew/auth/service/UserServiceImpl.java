package ru.proshik.jalmew.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.proshik.jalmew.auth.controller.dto.UserRequest;
import ru.proshik.jalmew.auth.repository.model.User;
import ru.proshik.jalmew.auth.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository repository;

    @Override
    public void create(UserRequest userDto) {

        User findUser = repository.findByUsername(userDto.getUsername());
        Assert.isNull(findUser, "user already exists: " + userDto.getUsername());

        String passwordHash = encoder.encode(userDto.getPassword());

        repository.save(new User(userDto.getUsername(), passwordHash));

        log.info("new user has been created: {}", userDto.getUsername());
    }
}
