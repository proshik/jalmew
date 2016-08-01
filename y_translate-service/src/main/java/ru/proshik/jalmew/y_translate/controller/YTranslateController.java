package ru.proshik.jalmew.y_translate.controller;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.proshik.jalmew.y_translate.client.YandexDictClient;
import ru.proshik.jalmew.y_translate.model.YTranslateWord;

import javax.annotation.PostConstruct;

/**
 * Created by proshik on 30.07.16.
 */
@RestController
@RequestMapping(value = "/yTranslate")
public class YTranslateController {

    @Value("${yandex.client.dict.key}")
    private String yandexKey;

    @Value("${yandex.client.dict.lang}")
    private String yandexLang;

    @Value("${yandex.client.dict.root.url}")
    private String yandexUrl;

    @Autowired
    private YandexDictClient yandexDictClient;

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(yandexKey) || StringUtils.isEmpty(yandexLang))
            throw new NullPointerException("params key or lang for y.dict is not set");
    }

    @RequestMapping(value = "translate", method = RequestMethod.GET, params = {"word"})
    public ResponseEntity translate(@RequestParam("word") @NotEmpty String word) {

        if (validate(word)) {
            YTranslateWord yTranslateWord = yandexDictClient.lookup(word);

            return ResponseEntity.ok(yTranslateWord);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validate(String word) {
        return !StringUtils.isEmpty(word);
    }

}
