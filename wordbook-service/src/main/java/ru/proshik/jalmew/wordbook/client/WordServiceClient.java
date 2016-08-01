package ru.proshik.jalmew.wordbook.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * Created by proshik on 30.07.16.
 */
@Component
@FeignClient("word-service")
public interface WordServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "word/{wordId}")
    String getById(@PathVariable("wordId") String wordId);

    @RequestMapping(method = RequestMethod.GET, value = "word")
    List<String> getByIds(@RequestParam("wordId") Set<String> wordIdsByByUser);
}
