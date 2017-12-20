package ru.proshik.jalmew.ytranslate.controller;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.proshik.jalmew.ytranslate.client.YandexDictClient;

/**
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

        if (StringUtils.isEmpty(word)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(yandexDictClient.lookup(word));
    }

}
