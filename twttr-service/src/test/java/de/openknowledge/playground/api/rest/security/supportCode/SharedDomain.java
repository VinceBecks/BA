package de.openknowledge.playground.api.rest.security.supportCode;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class SharedDomain {

    private Map<String, Account> accounts;
    private Map<String, String> passwordForUser = new HashMap<>();
    private Map<String, String> tokenMap = new HashMap<>();

    private Response response;

    public SharedDomain () {
        accounts = new HashMap<>();

        //todo: wie auslesen aus accounts.json?
        accounts.put("max", new Account(0, "max", "password", "Max", "Mustermann"));
        accounts.put("marta", new Account(1, "marta", "password", "Marta", "Musterfrau"));
        accounts.put("john", new Account(2, "john", "password", "John", "Doe"));
        accounts.put("jane", new Account(3, "jane", "password", "Jane", "Doe"));
        accounts.put("werner", new Account(4, "werner", "password", "Werner", "Pflanzen"));
        accounts.put("karl", new Account(5, "karl", "password", "Karl", "Ranseier"));
        accounts.put("lena", new Account(6, "lena", "password", "Lena", "Löchte"));
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

    public Account getAccount (String userName) {
        return accounts.get(userName);
    }
}