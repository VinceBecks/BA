package de.openknowledge.playground.api.rest.security.supportCode.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import de.openknowledge.playground.api.rest.security.domain.tweet.NewTweet;
import de.openknowledge.playground.api.rest.security.domain.tweet.TweetDTO;
import io.cucumber.datatable.DataTableType;

import java.util.Locale;
import java.util.Map;

public class Converter implements TypeRegistryConfigurer{
    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(NewTweet.class,
                (Map<String, String> row)-> {
                    if (row.keySet().contains("value")) {
                        return new NewTweet(row.get("value"));
                    }else {
                        return new NewTweet(row.get("content"));
                    }
                }));


        typeRegistry.defineDataTableType(new DataTableType (TweetDTO.class,
                (String s)-> new ObjectMapper().readValue(s, TweetDTO.class)));
    }
}
