package com.thekuzea.experimental.core.stepdef;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.thekuzea.experimental.core.fixture.DataKeys;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import com.thekuzea.experimental.domain.dao.UserRepository;
import com.thekuzea.experimental.domain.dto.UserDto;
import com.thekuzea.experimental.domain.enums.RestApiType;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.support.util.ApiAccessorHelper;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static com.thekuzea.experimental.support.constant.RestConstants.AUTHORIZATION;
import static com.thekuzea.experimental.support.constant.RestConstants.BEARER;
import static com.thekuzea.experimental.support.util.RestUtils.formToken;
import static com.thekuzea.experimental.support.util.RestUtils.formUri;

@RequiredArgsConstructor
public class UserStepDefinition {

    private final ScenarioContext scenarioContext;

    private final ApiAccessorHelper apiAccessorHelper;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ObjectMapper objectMapper;

    @Value("${experimental.security.default-role}")
    private String defaultRole;

    @Given("^create new user with (.*) username and (.*) password$")
    public void createNewUser(final String username, final String password) {
        final UserDto userDto = UserDto.builder()
                .username(username)
                .password(password)
                .build();

        scenarioContext.save(DataKeys.USER_RESOURCE, userDto);
    }

    @When("^send created user$")
    public void sendUser() {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final UserDto userDtoForSending = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserDto.class);

        final Response response = requestSpec
                .body(userDtoForSending)
                .post(apiAccessorHelper.getApiSubPathByType(RestApiType.USER))
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("^send created user once again$")
    public void sendUserOnceAgain() {
        sendUser();
    }

    @When("^search for user with (.+) username$")
    public void findUserByUserName(final String username) {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, formToken(BEARER, token))
                .get(formUri(apiAccessorHelper.getApiSubPathByType(RestApiType.USER), username)).thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("^remove user with (.+) username$")
    public void deleteUserByUserName(final String username) {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, formToken(BEARER, token))
                .delete(formUri(apiAccessorHelper.getApiSubPathByType(RestApiType.USER), username)).thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("^prepare user for update$")
    public void prepareUserForUpdate() {
        final String currentUserId = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class).getId().toString();
        final UserDto userDto = UserDto.builder()
                .id(currentUserId)
                .build();

        scenarioContext.save(DataKeys.USER_RESOURCE, userDto);
    }

    @When("^update user's (.+) username$")
    public void updateUsernameOfUser(final String username) {
        scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserDto.class).setUsername(username);
    }

    @When("^update user's (.+) password$")
    public void updatePasswordOfUser(final String password) {
        scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserDto.class).setPassword(password);
    }

    @When("^update user's (.+) role$")
    public void updateRoleOfUser(final String role) {
        scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserDto.class).setRole(role);
    }

    @Then("^send updated user$")
    public void sendUpdatedUser() {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final UserDto userDto = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserDto.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, formToken(BEARER ,token))
                .body(userDto)
                .put(formUri(apiAccessorHelper.getApiSubPathByType(RestApiType.USER), userDto.getId()))
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @Then("^save user in context$")
    public void saveUserInContext() {
        final UserDto userDto = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserDto.class);
        final Optional<User> foundUser = userRepository.findUserByUsername(userDto.getUsername());

        if (foundUser.isPresent()) {
            scenarioContext.save(DataKeys.USER_MODEL, foundUser.get());
        } else {
            fail("User hasn't been found in the database!");
        }
    }

    @Then("^validate new user entity$")
    public void validateSavedNewEntity() {
        final User actual = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class);
        final UserDto expected = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserDto.class);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
            softAssertions.assertThat(bCryptPasswordEncoder.matches(expected.getPassword(), actual.getPassword())).isTrue();
            softAssertions.assertThat(actual.getRole().getName()).isEqualTo(defaultRole);
        });
    }

    @Then("^validate received user entity$")
    public void validateReceivedEntity() {
        final Response response = scenarioContext.getDataByKey(DataKeys.RESPONSE, Response.class);
        final User expected = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class);

        try {
            final UserDto actual = objectMapper.readValue(response.getBody().asByteArray(), UserDto.class);

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getId()).isEqualTo(expected.getId().toString());
                softAssertions.assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
                softAssertions.assertThat(actual.getPassword()).isNull();
                softAssertions.assertThat(actual.getRole()).isEqualTo(expected.getRole().getName());
            });
        } catch (IOException e) {
            fail("Can't convert response body as byte array to object!");
        }
    }

    @Then("^validate updated user entity$")
    public void validateUpdatedEntity() {
        final User actual = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class);
        final UserDto expected = scenarioContext.getDataByKey(DataKeys.USER_RESOURCE, UserDto.class);
        int assertions = 0;

        if (!isBlank(expected.getUsername())) {
            assertThat("Usernames don't match!", actual.getUsername(), equalTo(expected.getUsername()));
            ++assertions;
        }
        if (!isBlank(expected.getPassword())) {
            assertTrue("Passwords don't match!", bCryptPasswordEncoder.matches(expected.getPassword(), actual.getPassword()));
            ++assertions;
        }
        if (!isBlank(expected.getRole())) {
            assertThat("Roles don't match!", actual.getRole().getName(), equalTo(expected.getRole()));
            ++assertions;
        }

        assertThat("At least one assertion must be done!", assertions, greaterThan(0));
    }

    @Then("^validate deleted user entity$")
    public void validateDeletedEntity() {
        final User contextEntity = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class);
        final Optional<User> actual = userRepository.findUserByUsername(contextEntity.getUsername());

        assertThat("User hasn't been deleted!", actual, equalTo(Optional.empty()));
    }
}
