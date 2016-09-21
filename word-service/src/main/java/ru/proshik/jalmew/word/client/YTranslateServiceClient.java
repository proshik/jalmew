package ru.proshik.jalmew.word.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.proshik.jalmew.word.client.ytranslate_dto.YTranslateWord;

/**
 * Created by proshik on 09.08.16.
 */
@Component
@FeignClient(name = "ytranslate-service")
public interface YTranslateServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "api/v1.0/translate")
    YTranslateWord translate(@RequestParam("word") String word);

}
