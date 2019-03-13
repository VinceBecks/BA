package de.openknowledge.playground.api.rest.security.supportCode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedDomain {

    private final String BASE_PATH = "http://localhost:8080/twttr-service/api";
    private Map<String, Account> accounts;
    private Map<String, String> accountCredentials = new HashMap<>();
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

    public void addAccountCredentials (String userName, String password) {
        this.accountCredentials.put(userName, password);
    }

    public Map<String, String> getAccountCredentials() {
        return accountCredentials;
    }

    public String basePath () {
        return BASE_PATH;
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