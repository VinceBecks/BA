package de.openknowledge.playground.api.rest.supportCode;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class SharedDomain {

    private Map<String, AccountCredentials> accounts;
    private Map<String, String> passwordForUser = new HashMap<>();
    private Map<String, String> tokenMap = new HashMap<>();

    private Response response;

    public SharedDomain () {
        accounts = new HashMap<>();

        accounts.put("max", new AccountCredentials(0, "max", "password", "Max", "Mustermann"));
        accounts.put("marta", new AccountCredentials(1, "marta", "password", "Marta", "Musterfrau"));
        accounts.put("john", new AccountCredentials(2, "john", "password", "John", "Doe"));
        accounts.put("jane", new AccountCredentials(3, "jane", "password", "Jane", "Doe"));
        accounts.put("werner", new AccountCredentials(4, "werner", "password", "Werner", "Pflanzen"));
        accounts.put("karl", new AccountCredentials(5, "karl", "password", "Karl", "Ranseier"));
        accounts.put("lena", new AccountCredentials(6, "lena", "password", "Lena", "Löchte"));
    }


    public void addValidToken (String userName, String token) {
        this.tokenMap.put(userName,token);
    }

    public String tokenFromAccount(String userName) {
        return tokenMap.get(userName);
    }

    public void setPasswordForUser(String userName, String password) {
        this.passwordForUser.put(userName, password);
    }

    public String getPasswordFromUser(String userName) {
        return passwordForUser.get(userName);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public AccountCredentials getAccount (String userName) {
        return accounts.get(userName);
    }

    public Map<String, AccountCredentials> getAccounts() {
        return accounts;
    }
}