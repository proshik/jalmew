package ru.proshik.jalmew.wordbook.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.proshik.jalmew.common.model.word.WordOut;
import ru.proshik.jalmew.common.model.word.WordShortOut;

import java.util.List;
import java.util.Set;

/**
 * Created by proshik on 30.07.16.
 */
@Component
@FeignClient(value = "word-service", decode404 = true)
public interface WordClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1.0/word/{text}")
    WordShortOut add(@PathVariable("text") String text);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1.0/word/{wordId}")
    WordOut getById(@PathVariable("wordId") String wordId);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1.0/word")
    List<WordOut> searchByIds(@RequestParam("wordId") Set<String> wordId);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1.0/word")
    List<WordOut> searchByText(@RequestParam("text") String text);

}
