package ru.proshik.jalmew.learn.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.proshik.jalmew.common.model.wordbook.WordbookShortOut;
import ru.proshik.jalmew.common.model.learn.TrainingWordResultRequest;

import java.util.List;

/**
 * Created by proshik on 21.08.16.
 */
@FeignClient("wordbook-service")
public interface WordbookClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1.0/wordbook/training/words/{userName}")
    List<WordbookShortOut> listForLearn(@PathVariable(value = "userName") String userName);

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            value = "/api/v1.0/wordbook/training/words/statistic/{userName}")
    void saveStatistic(@PathVariable(value = "userName") String userName, List<TrainingWordResultRequest> result);

}