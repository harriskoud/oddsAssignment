package com.test.oddschecker.utility;

import org.json.JSONObject;

public final class ControllerUtility {

    public static final String ODDS_BASE_PATH = "/v1/odds";
    public static final String ODDS_ACCEPTED = "Odds have been created for bet";
    public static final String INVALID_ODDS_FORMAT = "Invalid format of Odds";
    public static final String BET_NOT_FOUND = "Bet not found for given ID";
    public static final String INVALID_BET_ID = "Invalid Bet ID supplied";
    public static final String ODDS_RETURNED = "Odds are returned for bet ID";

    private ControllerUtility() {
        /*this class should not be instantiated */
    }

    public static JSONObject createJsonObject(final String message) {
        JSONObject responsePayload = new JSONObject();
        JSONObject responseMessage = new JSONObject();
        responseMessage.put("message", message);
        responsePayload.put("payload", responseMessage);
        return responsePayload;
    }
}
