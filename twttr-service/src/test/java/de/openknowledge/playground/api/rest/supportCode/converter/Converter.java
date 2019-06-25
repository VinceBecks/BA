package de.openknowledge.playground.api.rest.supportCode.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import de.openknowledge.playground.api.rest.security.application.tweet.DetailedTweet;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.NewTweet;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.TweetDTO;
import de.openknowledge.playground.api.rest.supportCode.SharedDomain;
import de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses.*;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.datatable.DataTableType;

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
                (Map<String, String> row)-> new NewTweet(row.get("value"))));


        typeRegistry.defineDataTableType(new DataTableType (TweetDTO.class,
                (String s)-> new ObjectMapper().readValue(s, TweetDTO.class)));


        typeRegistry.defineDataTableType(new DataTableType (DetailedTweet.class,
                (String s)-> new ObjectMapper().readValue(s, DetailedTweet.class)));


        typeRegistry.defineDataTableType(new DataTableType (ErrorMessage.class,
                (String s)-> new ObjectMapper().readValue(s, ErrorMessage.class)));

        typeRegistry.defineDataTableType(new DataTableType (AccountEntity.class,
                (Map<String, String> row)-> {
                    Integer id = Integer.parseInt(row.get("accountId"));
                    String userName = row.get("userName");
                    String firstName = row.get("firstName");
                    String lastName = row.get("lastName");
                    Integer role = row.get("role").equals("USER") ? 0 : row.get("role").equals("MODERATOR") ? 1 : null;
                    String accountType = role.equals(0) ? "USER" : role.equals(1) ? "MODERATOR" : null;
                    return new AccountEntity(accountType, id, userName, firstName, lastName, role);
                }));

        typeRegistry.defineDataTableType(new DataTableType (TweetEntity.class,
                (Map<String, String> row)-> {
                    Integer id = Integer.parseInt(row.get("tweetId"));
                    String content = row.get("content");
                    Integer state = row.get("state").equals("PUBLISH") ? 0 : row.get("state").equals("CANCELED") ? 1 : null;
                    String author = row.get("author") != null ? row.get("author") : null;
                    return new TweetEntity(id, content,null, state, author);
                }));

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
