package com.test.oddschecker.service;

import com.test.oddschecker.domain.Bet;
import com.test.oddschecker.domain.Odds;
import com.test.oddschecker.exception.OddsRetrievalBadRequestException;
import com.test.oddschecker.exception.OddsStorageBadRequestException;
import com.test.oddschecker.repository.OddsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.test.oddschecker.utility.ControllerUtility.*;

@Service
public class OddsService {

    @Autowired
    private OddsRepository oddsRepository;

    public ResponseEntity<String> storeOdds(final Odds odds) {

        if (validateOdds(odds)) {
            Odds persistentOdds = oddsRepository.save(odds);
            return ResponseEntity.status(HttpStatus.CREATED).body(ODDS_ACCEPTED);
        } else {
            throw new OddsStorageBadRequestException(INVALID_ODDS_FORMAT);
        }
    }

    public ResponseEntity<Bet> retrieveOddsByBetId(final Integer betId) {
        Optional<List<Odds>> odds = oddsRepository.findOddsByBetId(betId);
        if (odds.isPresent()) {
            List<Odds> filteredOdds = odds.get().stream().filter(odd -> odd.getBetId() == betId).collect(Collectors.toList());
            Bet bet = new Bet(filteredOdds);
            return ResponseEntity.ok(bet);
        } else {
            throw new OddsRetrievalBadRequestException(BET_NOT_FOUND);
        }
    }

    public boolean validateOdds(final Odds odds) {
        return odds.getOdds().matches("[1-9]*/[1-9][0-9]*")
                || odds.getOdds().matches("[A-Z][A-Z]");
    }
}
