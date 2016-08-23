package ru.proshik.jalmew.learn.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.proshik.jalmew.learn.client.dto.WordListOut;
import ru.proshik.jalmew.learn.client.dto.WordOutShort;

import java.util.List;

/**
 * Created by proshik on 21.08.16.
 */
@Component
@FeignClient("wordbook-service")
public interface WordbookClient {

    @RequestMapping(method = RequestMethod.GET, value = "wordbook/word")
    List<WordListOut> list();

}
