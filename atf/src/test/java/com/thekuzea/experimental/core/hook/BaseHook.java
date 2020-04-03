package com.thekuzea.experimental.core.hook;

import java.nio.file.Paths;
import java.sql.SQLException;
import javax.sql.DataSource;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.thekuzea.experimental.config.ContextConfig;
import com.thekuzea.experimental.core.fixture.DataKeys;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import com.thekuzea.experimental.support.util.ApiAccessorHelper;
import com.thekuzea.experimental.support.util.FileUtils;

@CucumberContextConfiguration
@SpringBootTest(classes = {ContextConfig.class})
@AutoConfigureDataJpa
@RequiredArgsConstructor
public class BaseHook {

    private final ScenarioContext scenarioContext;

    private final DataSource dataSource;

    private final ApiAccessorHelper apiAccessorHelper;

    @Before(order = 1)
    public void setUp() throws SQLException {
        final RequestSpecification requestSpecification = RestAssured.given()
                .log()
                .all()
                .baseUri(apiAccessorHelper.assembleUrl())
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        scenarioContext.save(DataKeys.PREDEFINED_REST_TEMPLATE, requestSpecification);
        FileUtils.loadSqlAndExecute(dataSource, Paths.get("sql", "cleanup.sql").toString());
    }

    @Before(value = "@InsertUserRole", order = 2)
    public void insertRoles() throws SQLException {
        FileUtils.loadSqlAndExecute(dataSource, Paths.get("sql", "insert_roles.sql").toString());
    }

    @Before(value = "@InitializeAdministrator", order = 3)
    public void insertAdminUser() throws SQLException {
        FileUtils.loadSqlAndExecute(dataSource, Paths.get("sql", "initialize_administrator.sql").toString());
    }

    @After(value = "@Clean")
    public void tearDown() {
        scenarioContext.clean();
    }
}
