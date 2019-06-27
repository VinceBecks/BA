package de.openknowledge.playground.api.rest.supportCode.domain;

public class GetUsersQueryParams {
    private String queryString;

    public GetUsersQueryParams (String searchString, String numUsers, String index) {
        StringBuilder sb= new StringBuilder();
        if (searchString != null || numUsers != null || index != null) sb.append("?");
        if (searchString != null ) sb.append("searchString=" + searchString + "&");
        if (numUsers != null ) sb.append("numUsers=" + numUsers + "&");
        if (index != null ) sb.append("index=" + index);
        this.queryString = sb.toString();
    }

    public String getQueryString () {
        return this.queryString;
    }
}
