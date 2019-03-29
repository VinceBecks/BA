package de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    String ACCOUNT_TYPE;

    Integer ACCOUNT_ID;

    String USERNAME;

    String FIRST_NAME;

    String LAST_NAME;

    Integer ROLE;

    public Account(String ACCOUNT_TYPE, Integer ACCOUNT_ID, String USERNAME, String FIRST_NAME, String LAST_NAME, Integer ROLE) {
        this.ACCOUNT_TYPE = ACCOUNT_TYPE;
        this.ACCOUNT_ID = ACCOUNT_ID;
        this.USERNAME = USERNAME;
        this.FIRST_NAME = FIRST_NAME;
        this.LAST_NAME = LAST_NAME;
        this.ROLE = ROLE;
    }


    @JsonProperty("ACCOUNT_TYPE")
    public String getACCOUNT_TYPE() {
        return ACCOUNT_TYPE;
    }

    public void setACCOUNT_TYPE(String ACCOUNT_TYPE) {
        this.ACCOUNT_TYPE = ACCOUNT_TYPE;
    }

    @JsonProperty("ACCOUNT_ID")
    public Integer getACCOUNT_ID() {
        return ACCOUNT_ID;
    }

    public void setACCOUNT_ID(Integer ACCOUNT_ID) {
        this.ACCOUNT_ID = ACCOUNT_ID;
    }

    @JsonProperty("USERNAME")
    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    @JsonProperty("FIRST_NAME")
    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    @JsonProperty("LAST_NAME")
    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
    }

    @JsonProperty("ROLE")
    public Integer getROLE() {
        return ROLE;
    }

    public void setROLE(Integer ROLE) {
        this.ROLE = ROLE;
    }


    @Override
    public String toString() {
        return "User{" +
                "ACCOUNT_TYPE='" + ACCOUNT_TYPE + '\'' +
                ", ACCOUNT_ID=" + ACCOUNT_ID +
                ", USERNAME='" + USERNAME + '\'' +
                ", FIRST_NAME='" + FIRST_NAME + '\'' +
                ", LAST_NAME='" + LAST_NAME + '\'' +
                ", ROLE=" + ROLE +
                '}';
    }
}
