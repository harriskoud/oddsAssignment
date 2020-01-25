package com.test.oddschecker.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.test.oddschecker.domain.Bet;
import com.test.oddschecker.domain.Odds;
import com.test.oddschecker.repository.OddsRepository;
import com.test.oddschecker.service.OddsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.test.oddschecker.utility.ControllerUtility.*;

@RestController
@RequestMapping(value = ODDS_BASE_PATH)
public class OddscheckerController {

    private static final Logger LOG = LoggerFactory.getLogger(OddscheckerController.class);

    @Autowired
    private OddsRepository oddsRepository;
    @Autowired
    private OddsService oddsService;

    @ApiOperation(value = "Offer odds for a bet")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = ODDS_ACCEPTED),
            @ApiResponse(code = 400, message = INVALID_ODDS_FORMAT)
    })
    @PostMapping
    public ResponseEntity<String> postOdds(@RequestBody final Odds odds) {
        LOG.info(String.format("Storing new odds $ for bet $"), odds.getOdds(), odds.getBetId());
        return oddsService.storeOdds(odds);
    }

    @ApiOperation(value = "Offer odds for a bet", response = Bet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ODDS_RETURNED),
            @ApiResponse(code = 400, message = INVALID_BET_ID),
            @ApiResponse(code = 404, message = BET_NOT_FOUND)
    })
    @GetMapping("/{betId}")
    public ResponseEntity<Bet> retrieveOdds(@PathVariable final Integer betId) {
        LOG.info(String.format("Quering new odds for bet $"), betId);
        return oddsService.retrieveOddsByBetId(betId);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException() {
        JSONObject responsePayload = new JSONObject();
        JSONObject responseMessage = new JSONObject();
        responseMessage.put("message", INVALID_BET_ID);
        responsePayload.put("payload", responseMessage);
        return new ResponseEntity(responsePayload.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException() {
        JSONObject responsePayload = new JSONObject();
        JSONObject responseMessage = new JSONObject();
        responseMessage.put("message", INVALID_BET_ID);
        responsePayload.put("payload", responseMessage);
        return new ResponseEntity(responsePayload.toString(), HttpStatus.BAD_REQUEST);
    }
}
