package de.openknowledge.playground.api.rest.security.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions( plugin = {"pretty"}, features="src/test/resources/features/", glue = {"de.openknowledge.playground.api.rest.security.stepDefinitions", "de.openknowledge.playground.api.rest.security.supportCode"})
public class RunAllFeaturesIT {

}