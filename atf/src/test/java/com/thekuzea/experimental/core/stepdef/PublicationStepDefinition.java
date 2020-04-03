package com.thekuzea.experimental.core.stepdef;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;

import com.thekuzea.experimental.core.fixture.DataKeys;
import com.thekuzea.experimental.core.fixture.ScenarioContext;
import com.thekuzea.experimental.domain.dao.PublicationRepository;
import com.thekuzea.experimental.domain.dto.PublicationDto;
import com.thekuzea.experimental.domain.enums.RestApiType;
import com.thekuzea.experimental.domain.enums.TopicType;
import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.domain.model.User;
import com.thekuzea.experimental.support.util.ApiAccessorHelper;

import static junit.framework.TestCase.fail;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import static com.thekuzea.experimental.domain.enums.TopicType.GENERATED;
import static com.thekuzea.experimental.support.constant.RestConstants.AUTHORIZATION;
import static com.thekuzea.experimental.support.constant.RestConstants.BEARER;
import static com.thekuzea.experimental.support.util.DateTimeUtils.convertOffsetDateTimeToString;
import static com.thekuzea.experimental.support.util.RestUtils.formToken;
import static com.thekuzea.experimental.support.util.RestUtils.formUri;

@RequiredArgsConstructor
public class PublicationStepDefinition {

    private static final String CURRENT_TIME = "now";

    private final ScenarioContext scenarioContext;

    private final ApiAccessorHelper apiAccessorHelper;

    private final PublicationRepository publicationRepository;

    private final ObjectMapper objectMapper;

    @Given("^create new publication with (.+) topic length and (.+) body length with publication time (.+)$")
    public void createNewPublication(final Integer topicLength, final Integer bodyLength, final String publicationTime) {
        String publicationDateTime = publicationTime;
        if (CURRENT_TIME.equals(publicationTime)) {
            publicationDateTime = convertOffsetDateTimeToString(OffsetDateTime.now());
        }

        final PublicationDto publicationDto = PublicationDto.builder()
                .publicationTime(publicationDateTime)
                .topic(randomAlphabetic(topicLength))
                .body(randomAlphabetic(bodyLength))
                .build();

        scenarioContext.save(DataKeys.PUBLICATION, publicationDto);
    }

    @When("^send created publication")
    public void sendPublication() {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final PublicationDto publicationDto = scenarioContext.getDataByKey(DataKeys.PUBLICATION, PublicationDto.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, formToken(BEARER, token))
                .body(publicationDto)
                .post(apiAccessorHelper.getApiSubPathByType(RestApiType.PUBLICATION))
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("send created publication once again")
    public void sendPublicationOnceAgain() {
        sendPublication();
    }

    @When("^remove publication with (.+) topic$")
    public void removePublicationByTopic(final TopicType topicType) {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final PublicationDto publicationDto = scenarioContext.getDataByKey(DataKeys.PUBLICATION, PublicationDto.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        String topic = "A super-amazing topic";
        if (GENERATED.equals(topicType)) {
            topic = publicationDto.getTopic();
        }

        final Response response = requestSpec.header(AUTHORIZATION, formToken(BEARER, token))
                .delete(formUri(apiAccessorHelper.getApiSubPathByType(RestApiType.PUBLICATION), topic))
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @When("^get all the publications for current logged in user$")
    public void getAllPublicationsForCurrentUser() {
        final RequestSpecification requestSpec = scenarioContext.getDataByKey(DataKeys.PREDEFINED_REST_TEMPLATE, RequestSpecification.class);
        final String token = scenarioContext.getDataByKey(DataKeys.TOKEN, String.class);

        final Response response = requestSpec.header(AUTHORIZATION, formToken(BEARER, token))
                .get(formUri(apiAccessorHelper.getApiSubPathByType(RestApiType.PUBLICATION), "current-user-publications"))
                .thenReturn();

        scenarioContext.save(DataKeys.RESPONSE, response);
    }

    @Then("^validate new publication entity$")
    public void validateSavedNewEntity() {
        final PublicationDto expected = scenarioContext.getDataByKey(DataKeys.PUBLICATION, PublicationDto.class);
        final Optional<Publication> actual = publicationRepository.findByTopic(expected.getTopic());

        if (actual.isPresent()) {
            actual.ifPresent(publication ->
                    SoftAssertions.assertSoftly(softAssertions -> {
                        softAssertions.assertThat(publication.getPublicationTime()).isEqualTo(expected.getPublicationTime());
                        softAssertions.assertThat(publication.getTopic()).isEqualTo(expected.getTopic());
                        softAssertions.assertThat(publication.getBody()).isEqualTo(expected.getBody());
                    }));
        } else {
            fail("Publication hasn't been found in the database!");
        }
    }

    @Then("^validate received publication entities$")
    public void validateReceivedEntity() {
        final Response response = scenarioContext.getDataByKey(DataKeys.RESPONSE, Response.class);
        final User currentUserLoggedIn = scenarioContext.getDataByKey(DataKeys.USER_MODEL, User.class);
        final List<Publication> expectedList = publicationRepository.findAllByPublishedBy(currentUserLoggedIn);

        try {
            final PublicationDto[] actualArray =
                    objectMapper.readValue(response.getBody().asByteArray(), PublicationDto[].class);

            final Map<String, PublicationDto> convertedActual = Arrays.stream(actualArray)
                    .collect(Collectors.toMap(PublicationDto::getId, resource -> resource));

            final AtomicInteger assertions = new AtomicInteger(0);
            expectedList.forEach(expected -> {
                final PublicationDto actual = convertedActual.get(expected.getId().toString());

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual.getId()).isEqualTo(expected.getId().toString());
                    softAssertions.assertThat(actual.getPublishedBy()).isEqualTo(expected.getPublishedBy().getUsername());

                    softAssertions.assertThat(actual.getPublicationTime())
                            .isEqualTo(convertOffsetDateTimeToString(expected.getPublicationTime()));

                    softAssertions.assertThat(actual.getTopic()).isEqualTo(expected.getTopic());
                    softAssertions.assertThat(actual.getBody()).isEqualTo(expected.getBody());
                });

                assertions.incrementAndGet();
            });

            assertThat("At least one assertion must be done!", assertions.get(), greaterThan(0));
        } catch (IOException e) {
            Assert.fail("Can't convert response body as byte array to array of objects!");
        }
    }

    @Then("^validate deleted publication entity$")
    public void validateDeletedEntity() {
        final PublicationDto contextRole = scenarioContext.getDataByKey(DataKeys.PUBLICATION, PublicationDto.class);
        final Optional<Publication> actual = publicationRepository.findByTopic(contextRole.getTopic());

        assertThat("Publication hasn't been deleted!", actual, equalTo(Optional.empty()));
    }
}
