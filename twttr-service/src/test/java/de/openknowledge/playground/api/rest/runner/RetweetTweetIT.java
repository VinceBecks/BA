package de.openknowledge.playground.api.rest.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features="src/test/resources/features/retweetTweet.feature", glue = {"de.openknowledge.playground.api.rest.stepDefinitions", "de.openknowledge.playground.api.rest.supportCode"})
public class RetweetTweetIT {
}
