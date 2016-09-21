package ru.proshik.jalmew.learn.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.proshik.jalmew.learn.client.dto.WordOutShort;

import java.util.List;
import java.util.Set;

/**
 * Created by proshik on 21.08.16.
 */
@Component
@FeignClient("word-service")
public interface WordClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1.0/word")
    List<WordOutShort> getByIds(@RequestParam("wordId") Set<String> wordId);

    // TODO: 22.09.16 убрать потом
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1.0/word/search")
    List<WordOutShort> search(@RequestParam("wordId") Set<String> wordId);

}
