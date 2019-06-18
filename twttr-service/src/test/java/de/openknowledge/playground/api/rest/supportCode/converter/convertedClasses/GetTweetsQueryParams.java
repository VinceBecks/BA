package de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses;

public class GetTweetsQueryParams {
    private String queryString;

    public GetTweetsQueryParams (String index, String numTweets) {
        StringBuilder sb= new StringBuilder();
        if (index != null || numTweets != null) sb.append("?");
        if (numTweets != null ) sb.append("numTweets=" + numTweets + "&");
        if (index != null ) sb.append("index=" + index);
        this.queryString = sb.toString();
    }

    public String getQueryString () {
        return queryString;
    }
}
