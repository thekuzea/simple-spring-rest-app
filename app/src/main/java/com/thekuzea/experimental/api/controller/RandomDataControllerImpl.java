package com.thekuzea.experimental.api.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomDataControllerImpl implements RandomDataController {

    @Override
    public String getRandomData(final Integer dataLength) {
        if (dataLength < (Integer.MAX_VALUE / 1000)) {
            return RandomStringUtils.randomAlphabetic(dataLength);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
