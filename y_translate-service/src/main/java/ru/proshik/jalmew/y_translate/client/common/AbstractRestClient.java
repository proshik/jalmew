package ru.proshik.jalmew.y_translate.client.common;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by proshik on 22.05.16.
 */
public abstract class AbstractRestClient {

    private RestTemplate template = new RestTemplate();

    public RestTemplate getTemplate() {
        return template;
    }
}
