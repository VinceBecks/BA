package de.openknowledge.playground.api.rest.security.supportCode;

import java.util.HashMap;
import java.util.Map;

public class SharedDomain {

    private Map<String, String> accountCredentials = new HashMap<>();
    private Map<String, String> tokenMap = new HashMap<>();

    public void addValidToken (String userName, String token) {
        this.tokenMap.put(userName,token);
    }

    public String tokenFromUser (String userName) {
        return tokenMap.get(userName);
    }

    public void addAccountCredentials (String userName, String password) {
        this.accountCredentials.put(userName, password);
    }

    public Map<String, String> getAccountCredentials() {
        return accountCredentials;
    }
}