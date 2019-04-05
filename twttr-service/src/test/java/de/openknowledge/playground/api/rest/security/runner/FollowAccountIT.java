package de.openknowledge.playground.api.rest.security.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features="src/test/resources/features/followUser.feature", glue = {"de.openknowledge.playground.api.rest.security.stepDefinition", "de.openknowledge.playground.api.rest.security.supportCode"})
public class FollowAccountIT {
}
