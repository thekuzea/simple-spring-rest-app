package com.thekuzea.experimental.core.stepdef;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

import com.thekuzea.experimental.core.fixture.DataKeys;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.domain.enums.RestApiType;
import com.thekuzea.experimental.support.util.ApiAccessorHelper;

@RequiredArgsConstructor
public class AuthenticationStepDefinition {

    private final ScenarioContext scenarioContext;

    private final ApiAccessorHelper apiAccessorHelper;

    @Then("^authenticate as (.*) user with password (.*)$")
    public void authenticate(final String username, final String password) {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final UserDto userDtoForSending = UserDto.builder()
                .username(username)
                .password(password)
                .build();

        final Response response = requestSpec.body(userDtoForSending)
                .post(apiAccessorHelper.getApiSubPathByType(RestApiType.AUTHENTICATION))
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @Then("^save token$")
    public void saveToken() {
        final Response response = scenarioContext.getDataByKey(DataKeys.RESPONSE, Response.class);
        final String token = response.getBody()
                .jsonPath()
                .getString("token");

        scenarioContext.save(DataKeys.TOKEN, token);
    }
}
