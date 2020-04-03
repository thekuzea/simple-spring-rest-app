package com.thekuzea.experimental.core.stepdef;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

import com.thekuzea.experimental.core.fixture.DataKeys;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import com.thekuzea.experimental.domain.dao.RoleRepository;
import com.thekuzea.experimental.domain.dto.RoleDto;
import com.thekuzea.experimental.domain.enums.RestApiType;
import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.support.util.ApiAccessorHelper;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static com.thekuzea.experimental.support.constant.RestConstants.AUTHORIZATION;
import static com.thekuzea.experimental.support.constant.RestConstants.BEARER;
import static com.thekuzea.experimental.support.util.RestUtils.formToken;
import static com.thekuzea.experimental.support.util.RestUtils.formUri;

@RequiredArgsConstructor
public class RoleStepDefinition {

    private final ScenarioContext scenarioContext;

    private final ApiAccessorHelper apiAccessorHelper;

    private final RoleRepository roleRepository;

    @Given("^create new role with (.+) name$")
    public void createNewRole(final String name) {
        final RoleDto roleDto = RoleDto.builder()
                .name(name)
                .build();

        scenarioContext.save(DataKeys.ROLE, roleDto);
    }

    @When("^send created role$")
    public void sendRole() {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final RoleDto roleDto = scenarioContext.getDataByKey(DataKeys.ROLE, RoleDto.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, formToken(BEARER, token))
                .body(roleDto)
                .post(apiAccessorHelper.getApiSubPathByType(RestApiType.ROLE))
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("^send created role once again$")
    public void sendRoleOnceAgain() {
        sendRole();
    }

    @When("^remove role with (.+) name$")
    public void deleteRoleByName(final String name) {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, formToken(BEARER, token))
                .delete(formUri(apiAccessorHelper.getApiSubPathByType(RestApiType.ROLE), name)).thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @Then("^validate new role entity$")
    public void validateSavedNewEntity() {
        final RoleDto expected = scenarioContext.getDataByKey(DataKeys.ROLE, RoleDto.class);
        final Optional<Role> actual = roleRepository.findRoleByName(expected.getName());

        if (actual.isPresent()) {
            assertThat("Role names don't match!", actual.get().getName(), equalTo(expected.getName()));
        } else {
            fail("Role haven't been found in the database!");
        }
    }

    @Then("^validate deleted role entity$")
    public void validateDeletedEntity() {
        final RoleDto contextRole = scenarioContext.getDataByKey(DataKeys.ROLE, RoleDto.class);
        final Optional<Role> actual = roleRepository.findRoleByName(contextRole.getName());

        assertThat("Role hasn't been deleted!", actual, equalTo(Optional.empty()));
    }
}
