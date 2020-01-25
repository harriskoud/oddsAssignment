package com.test.oddschecker.service;

import com.test.oddschecker.domain.Bet;
import com.test.oddschecker.domain.Odds;
import com.test.oddschecker.exception.OddsRetrievalBadRequestException;
import com.test.oddschecker.exception.OddsStorageBadRequestException;
import com.test.oddschecker.repository.OddsRepository;
import com.test.oddschecker.utility.ControllerUtility;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.test.oddschecker.utility.ControllerUtility.createJsonObject;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OddsServiceTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

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


    @MockBean
    private OddsRepository oddsRepository;
    @Autowired
    private OddsService oddsService;

    @Test
    public void storeOddsTestSuccess_validInput1() {
        Odds valid_odds = new Odds(1, BET_ID, USER_ID, VALID_INPUT1);
        when(oddsRepository.save(any())).thenReturn(valid_odds);
        ResponseEntity<String> response = oddsService.storeOdds(valid_odds);
        Assert.assertEquals(response.getStatusCodeValue(), HTTP_STATUS_OK);
        Assert.assertEquals(response.getBody(), createJsonObject(ODDS_ACCEPTED).toString());
    }

    @Test
    public void storeOddsTestSuccess_validInput2() {
        Odds valid_odds = new Odds(1, BET_ID, USER_ID, VALID_INPUT2);
        when(oddsRepository.save(any())).thenReturn(valid_odds);
        ResponseEntity<String> response = oddsService.storeOdds(valid_odds);
        Assert.assertEquals(response.getStatusCodeValue(), HTTP_STATUS_OK);
        Assert.assertEquals(response.getBody(), createJsonObject(ODDS_ACCEPTED).toString());
    }

    @Test
    public void storeOddsTestSuccess_validInput3() {
        Odds valid_odds = new Odds(1, BET_ID, USER_ID, VALID_INPUT3);
        when(oddsRepository.save(any())).thenReturn(valid_odds);
        ResponseEntity<String> response = oddsService.storeOdds(valid_odds);
        Assert.assertEquals(response.getStatusCodeValue(), HTTP_STATUS_OK);
        Assert.assertEquals(response.getBody(), createJsonObject(ODDS_ACCEPTED).toString());
    }

    @Test
    public void storeOddsTestFail_inValidInput1() {
        Odds valid_odds = new Odds(1, BET_ID, USER_ID, INVALID_INPUT1);
        exceptionRule.expect(OddsStorageBadRequestException.class);
        exceptionRule.expectMessage(IVALID_ODDS_FORMAT);
        ResponseEntity<String> response = oddsService.storeOdds(valid_odds);
    }

    @Test
    public void storeOddsTestFail_inValidInput2() {
        Odds valid_odds = new Odds(1, BET_ID, USER_ID, INVALID_INPUT2);
        exceptionRule.expect(OddsStorageBadRequestException.class);
        exceptionRule.expectMessage(IVALID_ODDS_FORMAT);
        ResponseEntity<String> response = oddsService.storeOdds(valid_odds);
    }

    @Test
    public void storeOddsTestFail_inValidInput3() {
        Odds valid_odds = new Odds(1, BET_ID, USER_ID, INVALID_INPUT3);
        exceptionRule.expect(OddsStorageBadRequestException.class);
        exceptionRule.expectMessage(IVALID_ODDS_FORMAT);
        ResponseEntity<String> response = oddsService.storeOdds(valid_odds);
    }

    @Test
    public void storeOddsTestFail_inValidInput4() {
        Odds valid_odds = new Odds(1, BET_ID, USER_ID, INVALID_INPUT4);
        exceptionRule.expect(OddsStorageBadRequestException.class);
        exceptionRule.expectMessage(IVALID_ODDS_FORMAT);
        ResponseEntity<String> response = oddsService.storeOdds(valid_odds);
    }

    @Test
    public void retrieveOdds_shouldReturn3() {
        when(oddsRepository.findOddsByBetId(any())).thenReturn(Optional.of(createOddsListSameBetId()));
        ResponseEntity<Bet> betResponseEntity = oddsService.retrieveOddsByBetId(1);
        Assert.assertEquals(betResponseEntity.getBody().getOddsList().size(), 3);
    }

    @Test
    public void retrieveOdds_shouldReturn1() {
        when(oddsRepository.findOddsByBetId(any())).thenReturn(Optional.of(Arrays.asList(new Odds(1, BET_ID_1, USER_ID, VALID_INPUT1))));
        ResponseEntity<Bet> betResponseEntity = oddsService.retrieveOddsByBetId(2);
        Assert.assertEquals(betResponseEntity.getBody().getOddsList().size(), 1);
    }

    @Test
    public void retrieveOdds_shouldThrowNotFoundException() {
        when(oddsRepository.findOddsByBetId(any())).thenReturn(Optional.empty());
        exceptionRule.expect(OddsRetrievalBadRequestException.class);
        exceptionRule.expectMessage(BET_NOT_FOUND);
        ResponseEntity<Bet> betResponseEntity = oddsService.retrieveOddsByBetId(29999);

    }

    private List<Odds> createOddsListSameBetId() {
        Odds odds1 = new Odds(1, BET_ID, USER_ID, VALID_INPUT1);
        Odds odds2 = new Odds(2, BET_ID, USER_ID, VALID_INPUT2);
        Odds odds3 = new Odds(3, BET_ID, USER_ID, VALID_INPUT2);
        return Arrays.asList(odds1, odds2, odds3);
    }

    private List<Odds> createOddsListRandomBetId() {
        Odds odds1 = new Odds(1, BET_ID, USER_ID, VALID_INPUT1);
        Odds odds2 = new Odds(2, BET_ID_1, USER_ID, VALID_INPUT2);
        Odds odds3 = new Odds(3, BET_ID, USER_ID, VALID_INPUT2);
        return Arrays.asList(odds1, odds2, odds3);
    }

}
