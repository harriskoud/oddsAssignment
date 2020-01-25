package com.test.oddschecker.controller;


import com.test.oddschecker.domain.Bet;
import com.test.oddschecker.domain.Odds;
import com.test.oddschecker.exception.OddsRetrievalBadRequestException;
import com.test.oddschecker.exception.OddsStorageBadRequestException;
import com.test.oddschecker.repository.OddsRepository;
import com.test.oddschecker.service.OddsService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.MissingFormatArgumentException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OddscheckerControllerTest {

    private static final String VALID_INPUT1 = "1/10";
    private static final String VALID_INPUT2 = "2/1";
    private static final String VALID_INPUT3 = "SP";
    private static final String INVALID_INPUT1 = "0/1";
    private static final String INVALID_INPUT2 = "SP/1";
    private static final String INVALID_INPUT3 = "2/3/1";
    private static final String INVALID_INPUT4 = "23*4";
    private static final String USER_ID = "USER_1";
    private static final Integer BET_ID = 1;
    private static final Integer BET_ID_1 = 2;
    private static final int HTTP_STATUS_OK = 201;
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    private static final int HTTP_STATUS_NOT_FOUND = 404;
    private static final String ODDS_ACCEPTED = "Odds have been created for bet";
    private static final String IVALID_ODDS_FORMAT = "Invalid format of Odds";
    private static final String BET_NOT_FOUND = "Bet not found for given ID";
    public static final String INVALID_BET_ID = "Invalid Bet ID supplied";


    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @MockBean
    private OddsService oddsService;
    @MockBean
    private OddsRepository oddsRepository;
    @Autowired
    private OddscheckerController oddscheckerController;


    @Test
    public void testSubmitOddsValidInput1() {
        Odds odds = new Odds(1, BET_ID, USER_ID, VALID_INPUT1);
        when(oddsService.storeOdds(any())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(ODDS_ACCEPTED));
        ResponseEntity<String> response = oddscheckerController.postOdds(odds);
        Assert.assertEquals(HTTP_STATUS_OK, response.getStatusCodeValue());
        Assert.assertEquals(response.getBody(), ODDS_ACCEPTED);
    }

    @Test
    public void testSubmitOddsValidInput2() {
        Odds odds = new Odds(1, BET_ID, USER_ID, VALID_INPUT2);
        when(oddsService.storeOdds(any())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(ODDS_ACCEPTED));
        ResponseEntity<String> response = oddscheckerController.postOdds(odds);
        Assert.assertEquals(HTTP_STATUS_OK, response.getStatusCodeValue());
        Assert.assertEquals(response.getBody(), ODDS_ACCEPTED);
    }

    @Test
    public void testSubmitOddsValidInput3() {
        Odds odds = new Odds(1, BET_ID, USER_ID, VALID_INPUT3);
        when(oddsService.storeOdds(any())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(ODDS_ACCEPTED));
        ResponseEntity<String> response = oddscheckerController.postOdds(odds);
        Assert.assertEquals(HTTP_STATUS_OK, response.getStatusCodeValue());
        Assert.assertEquals(response.getBody(), ODDS_ACCEPTED);
    }

    @Test
    public void testSubmitOddsInValidInput1() {
        Odds odds = new Odds(1, BET_ID, USER_ID, INVALID_INPUT1);
        when(oddsService.storeOdds(odds)).thenThrow(new OddsStorageBadRequestException(IVALID_ODDS_FORMAT));
        exceptionRule.expect(OddsStorageBadRequestException.class);
        exceptionRule.expectMessage(IVALID_ODDS_FORMAT);
        ResponseEntity<String> response = oddscheckerController.postOdds(odds);
    }

    @Test
    public void testSubmitOddsInValidInput2() {
        Odds odds = new Odds(1, BET_ID, USER_ID, INVALID_INPUT2);
        when(oddsService.storeOdds(odds)).thenThrow(new OddsStorageBadRequestException(IVALID_ODDS_FORMAT));
        exceptionRule.expect(OddsStorageBadRequestException.class);
        exceptionRule.expectMessage(IVALID_ODDS_FORMAT);
        ResponseEntity<String> response = oddscheckerController.postOdds(odds);
    }

    @Test
    public void testSubmitOddsInValidInput3() {
        Odds odds = new Odds(1, BET_ID, USER_ID, INVALID_INPUT3);
        when(oddsService.storeOdds(odds)).thenThrow(new OddsStorageBadRequestException(IVALID_ODDS_FORMAT));
        exceptionRule.expect(OddsStorageBadRequestException.class);
        exceptionRule.expectMessage(IVALID_ODDS_FORMAT);
        ResponseEntity<String> response = oddscheckerController.postOdds(odds);
    }

    @Test
    public void testSubmitOddsInValidInput4() {
        Odds odds = new Odds(1, BET_ID, USER_ID, INVALID_INPUT4);
        when(oddsService.storeOdds(odds)).thenThrow(new OddsStorageBadRequestException(IVALID_ODDS_FORMAT));
        exceptionRule.expect(OddsStorageBadRequestException.class);
        exceptionRule.expectMessage(IVALID_ODDS_FORMAT);
        ResponseEntity<String> response = oddscheckerController.postOdds(odds);
    }

    @Test
    public void testRetrieveOdds() {
        when(oddsService.retrieveOddsByBetId(any())).thenReturn(ResponseEntity.ok().body(createBet()));
        ResponseEntity<Bet> response = oddscheckerController.retrieveOdds(1);
        Assert.assertEquals(response.getStatusCodeValue(), 200);
        Assert.assertTrue(response.getBody().getOddsList().size() == 3);
    }

    @Test
    public void testRetrieveOdds_betIdNotFound() {
        when(oddsService.retrieveOddsByBetId(any())).thenThrow(new OddsRetrievalBadRequestException(BET_NOT_FOUND));
        exceptionRule.expect(OddsRetrievalBadRequestException.class);
        exceptionRule.expectMessage(BET_NOT_FOUND);
        ResponseEntity<?> response = oddscheckerController.retrieveOdds(1);
    }

    @Test
    public void testRetrieveOdds_InvalidBetId() {
        when(oddsService.retrieveOddsByBetId(any())).thenThrow(new MissingFormatArgumentException(INVALID_BET_ID));
        exceptionRule.expect(MissingFormatArgumentException.class);
        exceptionRule.expectMessage(INVALID_BET_ID);
        ResponseEntity<?> response = oddscheckerController.retrieveOdds(1);
    }

    private Bet createBet() {
        Odds odds1 = new Odds(1, BET_ID, USER_ID, VALID_INPUT1);
        Odds odds2 = new Odds(2, BET_ID, USER_ID, VALID_INPUT2);
        Odds odds3 = new Odds(3, BET_ID_1, USER_ID, VALID_INPUT2);
        return new Bet(Arrays.asList(odds1, odds2, odds3));
    }


}
