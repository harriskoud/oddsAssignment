package com.test.oddschecker.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.test.oddschecker.domain.Bet;
import com.test.oddschecker.domain.Odds;
import com.test.oddschecker.repository.OddsRepository;
import com.test.oddschecker.service.OddsService;
import com.test.oddschecker.utility.ControllerUtility;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
        LOG.info(String.format("Storing new odds %s for bet %s", odds.getOdds(), odds.getBetId()));
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
        LOG.info(String.format("Querying new odds for bet %s", betId));
        return oddsService.retrieveOddsByBetId(betId);
    }

    //They are placed here because we want only this controller to handle this way the following exceptions
    //If we wanted to have a global behaviour, then we would use the @ControllerAdvice
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException() {
        LOG.error("Attempt to store odds with invalid betId format in path variable");
        return new ResponseEntity(ControllerUtility.createJsonObject(INVALID_BET_ID).toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException() {
        LOG.error("Attempt to store odds with invalid betId format in request body");
        return new ResponseEntity(ControllerUtility.createJsonObject(INVALID_BET_ID).toString(), HttpStatus.BAD_REQUEST);
    }
}
