package de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses;

public class GetUsersQueryParams {
    private String queryString;

    public GetUsersQueryParams (String searchString, String numTweets, String index) {
        StringBuilder sb= new StringBuilder();
        if (searchString != null || numTweets != null || index != null) sb.append("?");
        if (searchString != null ) sb.append("searchString=" + searchString + "&");
        if (numTweets != null ) sb.append("numTweets=" + numTweets + "&");
        if (index != null ) sb.append("index=" + index);
        this.queryString = sb.toString();
    }

    public String getQueryString () {
        return this.queryString;
    }
}
