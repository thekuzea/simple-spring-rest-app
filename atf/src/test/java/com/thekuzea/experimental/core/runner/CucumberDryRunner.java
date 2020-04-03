package com.thekuzea.experimental.core.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.thekuzea.experimental.config",
                "com.thekuzea.experimental.core.hook",
                "com.thekuzea.experimental.core.stepdef"
        },
        tags = "not @Ignore",
        dryRun = true
)
public class CucumberDryRunner {

}
