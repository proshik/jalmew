package ru.proshik.jalmew.ytranslate.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.proshik.jalmew.common.model.ytranslate.Definition;
import ru.proshik.jalmew.common.model.ytranslate.YTranslateWordOut;
import ru.proshik.jalmew.ytranslate.client.YandexDictClient;

import java.util.Collections;

import static org.mockito.Mockito.when;

/**
 * Created by proshik on 04.09.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class YTranslateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private YandexDictClient yandexDictClient;
    @InjectMocks
    private YTranslateController yTranslateController = new YTranslateController();

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(yTranslateController).build();
    }

    @Test
    public void translateTest() throws Exception {

        YTranslateWordOut yTranslateWordOut = new YTranslateWordOut();
        ReflectionTestUtils.setField(yTranslateWordOut, "def", Collections.singletonList(new Definition()));

        when(yandexDictClient.lookup("word")).thenReturn(yTranslateWordOut);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/translate?word=word"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()));
    }

    @Test
    public void translateNotValueInQueryParamsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/translate?word="))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void translateNotQueryParamsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/translate"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
