package com.thekuzea.experimental.contract;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.thekuzea.experimental.contract.runner.ContractTestApplication;

import static io.restassured.RestAssured.DEFAULT_URI;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ContractTestApplication.class
)
@ActiveProfiles("contract")
public abstract class BaseContractTest {

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = String.format("%s:%s%s", DEFAULT_URI, port, contextPath);
    }
}
