package ru.proshik.jalmew.ytranslate.controller;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.proshik.jalmew.ytranslate.client.YandexDictClient;
import ru.proshik.jalmew.ytranslate.model.YTranslateWord;

/**
 *
 * Created by proshik on 30.07.16.
 */
@RestController
@RequestMapping(value = "/api/v1.0")
public class YTranslateController {

    @Autowired
    private YandexDictClient yandexDictClient;

    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(value = "translate", method = RequestMethod.GET)
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
