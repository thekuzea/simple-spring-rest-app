package com.thekuzea.experimental.core.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:target/cucumber"
        },
        glue = {
                "com.thekuzea.experimental.config",
                "com.thekuzea.experimental.core.hook",
                "com.thekuzea.experimental.core.stepdef"
        },
        features = "src/test/resources/features",
        stepNotifications = true,
        tags = "not @Ignore"
)
public class CucumberAllRunner {

}
