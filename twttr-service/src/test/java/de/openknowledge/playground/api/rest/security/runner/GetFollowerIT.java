package de.openknowledge.playground.api.rest.security.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import net.jcip.annotations.NotThreadSafe;
import org.junit.runner.RunWith;

@NotThreadSafe
@RunWith(Cucumber.class)
@CucumberOptions(tags= "@just", plugin = {"pretty"}, features="src/test/resources/features/getFollower.feature", glue = {"de.openknowledge.playground.api.rest.security.stepDefinition", "de.openknowledge.playground.api.rest.security.supportCode"})
public class GetFollowerIT {
}
