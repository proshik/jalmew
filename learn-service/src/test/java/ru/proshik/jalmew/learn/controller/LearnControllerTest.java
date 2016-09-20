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
import ru.proshik.jalmew.learn.client.WordClient;
import ru.proshik.jalmew.learn.client.WordbookClient;
import ru.proshik.jalmew.learn.client.dto.WordListOut;
import ru.proshik.jalmew.learn.client.dto.WordOutShort;
import ru.proshik.jalmew.learn.controller.dto.AnswerTranslateWord;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


    private List<WordOutShort> wordOutShortFromWordService;
    private List<WordListOut> wordListOutFromWordbookService;
    private List<AnswerTranslateWord> answerResultList;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(learnController).build();
    }

    @Test
    public void wordTranslateTest() throws Exception {
        initDataWordTranslate();

        when(wordbookClient.listForLearn("userName")).thenReturn(wordListOutFromWordbookService);
        when(wordClient.getByIds(anySet())).thenReturn(wordOutShortFromWordService);

        mockMvc.perform(get("/api/v1.0/learn/training/words").principal(getPrincipcal()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.words[*]", hasSize(10)))
                .andExpect(jsonPath("$.words[0].saltWords[*]", hasSize(5)))
                .andExpect(jsonPath("$.words[5].saltWords[*]", hasSize(5)))
                .andExpect(jsonPath("$.words[0].wordId", notNullValue()))
                .andExpect(jsonPath("$.words[0].text", notNullValue()))
                .andExpect(jsonPath("$.words[0].translate", notNullValue()))
                .andExpect(jsonPath("$.words[0].trs", notNullValue()))
                .andExpect(jsonPath("$.words[0].progressPercent", notNullValue()));

        /**
         * For equals object
         * -----------------


         LearnWord firstWord = new LearnWord("123456", "future", "будущее", "[trs]", 25,
         Arrays.asList("first", "second", "third", "four", "five"));
         LearnWord secondWord = new LearnWord("123457", "word", "слово", "[trs]", 0,
         Arrays.asList("first", "second", "third", "four", "five"));

         LearnTranslateWord expectedResponse; expectedResponse = new LearnTranslateWord(Arrays.asList(firstWord, secondWord));

         ObjectMapper mapper = new ObjectMapper();
         String expectedResult = mapper.writeValueAsString(expectedResponse);

         mockMvc.perform(get("/learn/training/words"))
         .andExpect(MockMvcResultMatchers.content().json(expectedResult));
         */
    }

    @Test
    public void wordTranslateLessWordsInWordbookUserTest() throws Exception {
        when(wordbookClient.listForLearn("userName")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1.0/learn/training/words").principal(getPrincipcal()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void trainingWordAnswer() throws Exception {
        initDataTrainingWordAnswer();

        mockMvc.perform(post("/api/v1.0/learn/training/words")
                .principal(getPrincipcal())
                .content(mapper.writeValueAsString(answerResultList))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    private void initDataWordTranslate() {

        WordOutShort out2 = new WordOutShort("123457", "future2", "будущее", "[trs]");
        WordOutShort out3 = new WordOutShort("123458", "future3", "будущее", "[trs]");
        WordOutShort out4 = new WordOutShort("123459", "future4", "будущее", "[trs]");
        WordOutShort out5 = new WordOutShort("123460", "future5", "будущее", "[trs]");
        WordOutShort out6 = new WordOutShort("123461", "future6", "будущее", "[trs]");
        WordOutShort out7 = new WordOutShort("123462", "future7", "будущее", "[trs]");
        WordOutShort out8 = new WordOutShort("123463", "future8", "будущее", "[trs]");
        WordOutShort out9 = new WordOutShort("123464", "future9", "будущее", "[trs]");
        WordOutShort out10 = new WordOutShort("123465", "future10", "будущее", "[trs]");
        WordOutShort out11 = new WordOutShort("123466", "future11", "будущее", "[trs]");
        wordOutShortFromWordService = Arrays.asList(out2, out3, out4, out5, out6, out7, out8, out9, out10, out11);

        WordListOut outList1 = new WordListOut("123456", 100);
        WordListOut outList2 = new WordListOut("123457", 0);
        WordListOut outList3 = new WordListOut("123458", 50);
        WordListOut outList4 = new WordListOut("123459", 0);
        WordListOut outList5 = new WordListOut("123460", 75);
        WordListOut outList6 = new WordListOut("123461", 25);
        WordListOut outList7 = new WordListOut("123462", 75);
        WordListOut outList8 = new WordListOut("123463", 25);
        WordListOut outList9 = new WordListOut("123464", 50);
        WordListOut outList10 = new WordListOut("123465", 75);
        WordListOut outList11 = new WordListOut("123466", 50);
        wordListOutFromWordbookService = Arrays.asList(outList1, outList2, outList3, outList4, outList5, outList6,
                outList7, outList8, outList9, outList10, outList11);
    }

    private void initDataTrainingWordAnswer() {
        AnswerTranslateWord answer1 = new AnswerTranslateWord("123457", true);
        AnswerTranslateWord answer2 = new AnswerTranslateWord("123458", true);
        AnswerTranslateWord answer3 = new AnswerTranslateWord("123459", true);
        AnswerTranslateWord answer4 = new AnswerTranslateWord("123460", true);
        AnswerTranslateWord answer5 = new AnswerTranslateWord("123461", true);
        AnswerTranslateWord answer6 = new AnswerTranslateWord("123462", true);
        AnswerTranslateWord answer7 = new AnswerTranslateWord("123463", true);
        AnswerTranslateWord answer8 = new AnswerTranslateWord("123464", true);
        AnswerTranslateWord answer9 = new AnswerTranslateWord("123465", true);
        AnswerTranslateWord answer10 = new AnswerTranslateWord("123466", true);

        answerResultList = Arrays.asList(answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8,
                answer9, answer10);

    }

    public Principal getPrincipcal() {
        return () -> "userName";
    }
}