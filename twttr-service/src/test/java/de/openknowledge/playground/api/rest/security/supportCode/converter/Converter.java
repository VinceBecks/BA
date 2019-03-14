package de.openknowledge.playground.api.rest.security.supportCode.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import de.openknowledge.playground.api.rest.security.domain.account.AccountType;
import de.openknowledge.playground.api.rest.security.domain.accounts.UserDTO;
import de.openknowledge.playground.api.rest.security.domain.tweet.NewTweet;
import de.openknowledge.playground.api.rest.security.domain.tweet.TweetDTO;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.ErrorMessage;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.GetTweetsQueryParams;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.GetUsersQueryParams;
import de.openknowledge.playground.api.rest.security.supportCode.converter.convertedClasses.IntegerList;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.datatable.DataTableType;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Converter implements TypeRegistryConfigurer{
    private SharedDomain domain = new SharedDomain();



    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(NewTweet.class,
                (Map<String, String> row)-> {
                    //todo: ist der if/else Teil noch relevant?
                    if (row.keySet().contains("value")) {
                        return new NewTweet(row.get("value"));
                    }else {
                        return new NewTweet(row.get("content"));
                    }
                }));



        typeRegistry.defineDataTableType(new DataTableType (TweetDTO.class,
                (String s)-> new ObjectMapper().readValue(s, TweetDTO.class)));

        typeRegistry.defineDataTableType(new DataTableType (ErrorMessage.class,
                (String s)-> new ObjectMapper().readValue(s, ErrorMessage.class)));


        typeRegistry.defineDataTableType(new DataTableType (GetTweetsQueryParams.class,
                (Map<String, String> row)-> {
                    String numTweets = row.get("numTweets").equals("not setted") ? null : row.get("numTweets");
                    String index = row.get("index").equals("not setted") ? null : row.get("index");
                    return new GetTweetsQueryParams(index, numTweets);
                }));


        typeRegistry.defineDataTableType(new DataTableType (GetUsersQueryParams.class,
                (Map<String, String> row)-> {
                    String s = row.get("searchString");
                    String serachString = null;
                    if (s != null) { serachString = row.get("searchString").equals("not setted")  ? null : row.get("searchString"); }

                    String numTweets = row.get("numTweets").equals("not setted") ? null : row.get("numTweets");
                    String index = row.get("index").equals("not setted") ? null : row.get("index");
                    return new GetUsersQueryParams(serachString, numTweets, index);
                }));

        typeRegistry.defineParameterType(new ParameterType<IntegerList>(
                "Ids",
                "([0-9]{0,2}(,[0-9])*)",
                IntegerList.class,
                IntegerList::new
        ));
    }
}
