package com.test.oddschecker.service;

import com.test.oddschecker.domain.Bet;
import com.test.oddschecker.domain.Odds;
import com.test.oddschecker.exception.OddsRetrievalBadRequestException;
import com.test.oddschecker.exception.OddsStorageBadRequestException;
import com.test.oddschecker.repository.OddsRepository;
import com.test.oddschecker.utility.ControllerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.test.oddschecker.utility.ControllerUtility.*;

@Service
public class OddsService {

    private static final Logger LOG = LoggerFactory.getLogger(OddsService.class);

    @Autowired
    private OddsRepository oddsRepository;

    public ResponseEntity<String> storeOdds(final Odds odds) {
        if (validateOdds(odds)) {
            Odds persistentOdds = oddsRepository.save(odds);
            LOG.info("Stored: " + odds.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(ControllerUtility.createJsonObject(ODDS_ACCEPTED).toString());
        } else {
            throw new OddsStorageBadRequestException(INVALID_ODDS_FORMAT);
        }
    }

    public ResponseEntity<Bet> retrieveOddsByBetId(final Integer betId) {
        Optional<List<Odds>> filteredOdds = oddsRepository.findOddsByBetId(betId);
        if (filteredOdds.isPresent()) {
            LOG.info("Fetched: " + filteredOdds.toString());
            Bet bet = new Bet(filteredOdds.get());
            return ResponseEntity.ok(bet);
        } else {
            LOG.error(String.format("Requested betId : %s was not found", betId));
            throw new OddsRetrievalBadRequestException(BET_NOT_FOUND);
        }
    }

    private boolean validateOdds(final Odds odds) {
        LOG.info(String.format("Validating odds %s format", odds.getOdds()));
        return odds.getOdds().matches("[1-9]*/[1-9][0-9]*")
                || odds.getOdds().matches("[A-Z][A-Z]");
    }
}
