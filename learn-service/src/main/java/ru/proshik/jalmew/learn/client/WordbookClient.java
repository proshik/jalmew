package ru.proshik.jalmew.learn.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.proshik.jalmew.learn.client.dto.WordListOut;
import ru.proshik.jalmew.learn.controller.dto.AnswerTranslateWord;

import java.util.List;

/**
 * Created by proshik on 21.08.16.
 */
@Service
@FeignClient("wordbook-service")
public interface WordbookClient {

    @RequestMapping(method = RequestMethod.GET, value = "wordbook/word/learn/{userName}")
    List<WordListOut> listForLearn(@PathVariable(value = "userName") String userName);

    @RequestMapping(method = RequestMethod.POST, value = "wordbook/word/learn/{userName}")
    void saveStatistic(@PathVariable(value = "userName") String userName, List<AnswerTranslateWord> result);

}