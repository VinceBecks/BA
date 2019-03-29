package de.openknowledge.playground.api.rest.security.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(tags= "@just", plugin = {"pretty"}, features="src/test/resources/features/getTweets.feature", glue = {"de.openknowledge.playground.api.rest.security.stepDefinition", "de.openknowledge.playground.api.rest.security.supportCode"})
public class GetTweetsIT {
}
