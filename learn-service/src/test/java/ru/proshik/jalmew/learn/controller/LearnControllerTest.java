package ru.proshik.jalmew.learn.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.proshik.jalmew.common.model.learn.TrainingWordResultRequest;
import ru.proshik.jalmew.common.model.word.Ex;
import ru.proshik.jalmew.common.model.word.Tr;
import ru.proshik.jalmew.common.model.word.WordOut;
import ru.proshik.jalmew.common.model.wordbook.WordbookShortOut;
import ru.proshik.jalmew.learn.client.WordClient;
import ru.proshik.jalmew.learn.client.WordbookClient;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by proshik on 20.08.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class LearnControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WordClient wordClient;
    @Mock
    private WordbookClient wordbookClient;
    @InjectMocks
    private LearnController learnController;

    private ObjectMapper mapper = new ObjectMapper();

    private List<WordOut> wordOutShortFromWordServiceOut;
    private List<WordbookShortOut> wordListOutFromWordbookService;
    private List<TrainingWordResultRequest> answerResultList;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(learnController).build();
    }

    @Test
    public void wordTranslateTest() throws Exception {
        initDataWordTranslate();

        when(wordbookClient.listForLearn("userName")).thenReturn(wordListOutFromWordbookService);
        when(wordClient.search(anySet())).thenReturn(wordOutShortFromWordServiceOut);

        mockMvc.perform(get("/api/v1.0/learn/training/words").principal(getPrincipal()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.words[*]", hasSize(10)))
                .andExpect(jsonPath("$.words[0].saltWords[*]", hasSize(5)))
                .andExpect(jsonPath("$.words[5].saltWords[*]", hasSize(5)))
                .andExpect(jsonPath("$.words[0].wordId", notNullValue()))
                .andExpect(jsonPath("$.words[0].text", notNullValue()))
                .andExpect(jsonPath("$.words[*].translate", notNullValue()))
                .andExpect(jsonPath("$.words[0].trs", notNullValue()))
                .andExpect(jsonPath("$.words[0].progressPercent", notNullValue()));

        /**
         * For equals object
         * -----------------


         WordForLearnOut firstWord = new WordForLearnOut("123456", "future", "будущее", "[trs]", 25,
         Arrays.asList("first", "second", "third", "four", "five"));
         WordForLearnOut secondWord = new WordForLearnOut("123457", "word", "слово", "[trs]", 0,
         Arrays.asList("first", "second", "third", "four", "five"));

         TrainingWordOut expectedResponse; expectedResponse = new TrainingWordOut(Arrays.asList(firstWord, secondWord));

         ObjectMapper mapper = new ObjectMapper();
         String expectedResult = mapper.writeValueAsString(expectedResponse);

         mockMvc.perform(get("/learn/training/words"))
         .andExpect(MockMvcResultMatchers.content().json(expectedResult));
         */
    }

    @Test
    public void wordTranslateLessWordsInWordbookUserTest() throws Exception {
        when(wordbookClient.listForLearn("userName")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1.0/learn/training/words").principal(getPrincipal()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void trainingWordAnswer() throws Exception {
        initDataTrainingWordAnswer();

        mockMvc.perform(put("/api/v1.0/learn/training/words")
                .principal(getPrincipal())
                .content(mapper.writeValueAsString(answerResultList))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    private void initDataWordTranslate() {

        Ex ex = new Ex("test", "translate");

        Tr tr1 = new Tr("text", "translate", "trs", "partOfSpeech", "get", Collections.singletonList(ex));
        Tr tr2 = new Tr("text", "translate", "trs", "partOfSpeech", "get", Collections.singletonList(ex));

        WordOut out2 = new WordOut("123457", "future2", null, null, Arrays.asList(tr1, tr2));
        WordOut out3 = new WordOut("123458", "future3", null, null, Arrays.asList(tr1, tr2));
        WordOut out4 = new WordOut("123459", "future4", null, null, Arrays.asList(tr1, tr2));
        WordOut out5 = new WordOut("123460", "future5", null, null, Arrays.asList(tr1, tr2));
        WordOut out6 = new WordOut("123461", "future6", null, null, Arrays.asList(tr1, tr2));
        WordOut out7 = new WordOut("123462", "future7", null, null, Arrays.asList(tr1, tr2));
        WordOut out8 = new WordOut("123463", "future8", null, null, Arrays.asList(tr1, tr2));
        WordOut out9 = new WordOut("123464", "future9", null, null, Arrays.asList(tr1, tr2));
        WordOut out10 = new WordOut("123465", "future10", null, null, Arrays.asList(tr1, tr2));
        WordOut out11 = new WordOut("123466", "future11", null, null, Arrays.asList(tr1, tr2));
        wordOutShortFromWordServiceOut = Arrays.asList(out2, out3, out4, out5, out6, out7, out8, out9, out10, out11);

        WordbookShortOut outList1 = new WordbookShortOut("123456", 100);
        WordbookShortOut outList2 = new WordbookShortOut("123457", 0);
        WordbookShortOut outList3 = new WordbookShortOut("123458", 50);
        WordbookShortOut outList4 = new WordbookShortOut("123459", 0);
        WordbookShortOut outList5 = new WordbookShortOut("123460", 75);
        WordbookShortOut outList6 = new WordbookShortOut("123461", 25);
        WordbookShortOut outList7 = new WordbookShortOut("123462", 75);
        WordbookShortOut outList8 = new WordbookShortOut("123463", 25);
        WordbookShortOut outList9 = new WordbookShortOut("123464", 50);
        WordbookShortOut outList10 = new WordbookShortOut("123465", 75);
        WordbookShortOut outList11 = new WordbookShortOut("123466", 50);
        wordListOutFromWordbookService = Arrays.asList(outList1, outList2, outList3, outList4, outList5, outList6,
                outList7, outList8, outList9, outList10, outList11);
    }

    private void initDataTrainingWordAnswer() {
        TrainingWordResultRequest answer1 = new TrainingWordResultRequest("123457", true);
        TrainingWordResultRequest answer2 = new TrainingWordResultRequest("123458", true);
        TrainingWordResultRequest answer3 = new TrainingWordResultRequest("123459", true);
        TrainingWordResultRequest answer4 = new TrainingWordResultRequest("123460", true);
        TrainingWordResultRequest answer5 = new TrainingWordResultRequest("123461", true);
        TrainingWordResultRequest answer6 = new TrainingWordResultRequest("123462", true);
        TrainingWordResultRequest answer7 = new TrainingWordResultRequest("123463", true);
        TrainingWordResultRequest answer8 = new TrainingWordResultRequest("123464", true);
        TrainingWordResultRequest answer9 = new TrainingWordResultRequest("123465", true);
        TrainingWordResultRequest answer10 = new TrainingWordResultRequest("123466", true);

        answerResultList = Arrays.asList(answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8,
                answer9, answer10);

    }

    public Principal getPrincipal() {
        return () -> "userName";
    }
}