package ru.proshik.jalmew.wordbook.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.proshik.jalmew.wordbook.controller.model.UserRequest;

@Component
@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/uaa/users",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void createUser(UserRequest user);

}
