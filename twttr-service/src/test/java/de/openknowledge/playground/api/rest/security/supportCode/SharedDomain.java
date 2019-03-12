package de.openknowledge.playground.api.rest.security.supportCode;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class SharedDomain {

    private final String BASE_PATH = "http://localhost:8080/twttr-service/api";
    private Map<String, String> accountCredentials = new HashMap<>();
    private Map<String, String> tokenMap = new HashMap<>();

    private Response response;


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

    public String basePath () {
        return BASE_PATH;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}