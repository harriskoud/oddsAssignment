package com.test.oddschecker.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.oddschecker.domain.Bet;
import com.test.oddschecker.domain.Odds;
import com.test.oddschecker.exception.OddsRetrievalBadRequestException;
import com.test.oddschecker.exception.OddsStorageBadRequestException;
import com.test.oddschecker.repository.OddsRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class OddscheckerITest {

    @Autowired
    private OddsRepository oddsRepository;
    @Autowired
    private OddscheckerController oddscheckerController;

    private RestTemplate restTemplate;
    private MockMvc mvc;

    private static final String VALID_INPUT1 = "1/10";
    private static final String VALID_INPUT2 = "2/1";
    private static final String VALID_INPUT3 = "SP";
    private static final String USER_ID = "USER_1";
    private static final Integer BET_ID = 1;
    private static final Integer BET_ID_1 = 2;
    private static final String ODDS_ACCEPTED = "Odds have been created for bet";
    private static final String IVALID_ODDS_FORMAT = "Invalid format of Odds";
    private static final String BET_NOT_FOUND = "Bet not found for given ID";
    public static final String INVALID_BET_ID = "Invalid Bet ID supplied";

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(oddscheckerController).build();
        MockMvcBuilders.standaloneSetup(oddsRepository).build();
        restTemplate = new RestTemplate();
    }

    @Test
    public void storeOddsValidInput() throws Exception {
        MvcResult result = mvc.perform(post("/odds")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "  \"betId\": \"1\",\n" +
                        "  \"odds\": \"1/10\",\n" +
                        "  \"userId\": \"Harris\"\n" +
                        "}"))
                .andExpect(status().isCreated()).andExpect(content().string(ODDS_ACCEPTED)).andReturn();
    }

    @Test
    public void storeOddsInValidBetID() throws Exception {
        MvcResult result = mvc.perform(post("/odds")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "  \"betId\": \"test\",\n" +
                        "  \"odds\": \"1/10\",\n" +
                        "  \"userId\": \"Harris\"\n" +
                        "}"))
                .andExpect(status().isBadRequest()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodeResponse = mapper.readTree(result.getResponse().getContentAsString());
        Assert.assertEquals(INVALID_BET_ID, nodeResponse.get("payload").get("message").textValue());
    }

    @Test
    public void storeOddsInValidInput1() throws Exception {
        MvcResult result = mvc.perform(post("/odds")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "  \"betId\": \"1\",\n" +
                        "  \"odds\": \"0/10\",\n" +
                        "  \"userId\": \"Harris\"\n" +
                        "}"))
                .andExpect(status().isBadRequest()).andReturn();
        Optional<OddsStorageBadRequestException> exception = Optional.ofNullable((OddsStorageBadRequestException) result.getResolvedException());

        exception.ifPresent((se) -> assertThat(se.getMessage(), is(IVALID_ODDS_FORMAT)));
        exception.ifPresent((se) -> assertThat(se, is(notNullValue())));
        exception.ifPresent((se) -> assertThat(se, is(instanceOf(OddsStorageBadRequestException.class))));

    }

    @Test
    public void retrieveBetwith3Odds() throws Exception {
        Odds odds1 = new Odds(1, BET_ID, USER_ID, VALID_INPUT1);
        Odds odds2 = new Odds(2, BET_ID, USER_ID, VALID_INPUT2);
        Odds odds3 = new Odds(3, BET_ID, USER_ID, VALID_INPUT2);

        oddsRepository.save(odds1);
        oddsRepository.save(odds2);
        oddsRepository.save(odds3);

        MvcResult result = mvc.perform(get("/odds/" + BET_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodeResponse = mapper.readTree(result.getResponse().getContentAsString());
        Bet bet = mapper.treeToValue(nodeResponse, Bet.class);
        Assert.assertEquals(bet.getOddsList().size(), 3);

    }

    @Test
    public void retrieveBetwith1Odd() throws Exception {
        Odds odds1 = new Odds(1, BET_ID_1, USER_ID, VALID_INPUT1);
        Odds odds2 = new Odds(2, BET_ID, USER_ID, VALID_INPUT2);
        Odds odds3 = new Odds(3, BET_ID, USER_ID, VALID_INPUT2);

        oddsRepository.save(odds1);
        oddsRepository.save(odds2);
        oddsRepository.save(odds3);

        MvcResult result = mvc.perform(get("/odds/" + BET_ID_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodeResponse = mapper.readTree(result.getResponse().getContentAsString());
        Bet bet = mapper.treeToValue(nodeResponse, Bet.class);
        Assert.assertEquals(bet.getOddsList().size(), 1);

    }

    @Test
    public void retrieveOddsNoBetFound() throws Exception {
        Odds odds1 = new Odds(1, BET_ID_1, USER_ID, VALID_INPUT1);
        Odds odds2 = new Odds(2, BET_ID, USER_ID, VALID_INPUT2);
        Odds odds3 = new Odds(3, BET_ID, USER_ID, VALID_INPUT2);

        oddsRepository.save(odds1);
        oddsRepository.save(odds2);
        oddsRepository.save(odds3);

        MvcResult result = mvc.perform(get("/odds/9999")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound()).andReturn();
        Optional<OddsRetrievalBadRequestException> exception = Optional.ofNullable((OddsRetrievalBadRequestException) result.getResolvedException());

        exception.ifPresent((se) -> assertThat(se.getMessage(), is(BET_NOT_FOUND)));
        exception.ifPresent((se) -> assertThat(se, is(notNullValue())));
        exception.ifPresent((se) -> assertThat(se, is(instanceOf(OddsRetrievalBadRequestException.class))));

    }

    @Test
    public void retrieveOddsInvalidBetIdFormat() throws Exception {
        Odds odds1 = new Odds(1, BET_ID_1, USER_ID, VALID_INPUT1);
        Odds odds2 = new Odds(2, BET_ID, USER_ID, VALID_INPUT2);
        Odds odds3 = new Odds(3, BET_ID, USER_ID, VALID_INPUT2);

        oddsRepository.save(odds1);
        oddsRepository.save(odds2);
        oddsRepository.save(odds3);

        MvcResult result = mvc.perform(get("/odds/euhfhhf")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodeResponse = mapper.readTree(result.getResponse().getContentAsString());
        Assert.assertEquals(INVALID_BET_ID, nodeResponse.get("payload").get("message").textValue());

    }
}
