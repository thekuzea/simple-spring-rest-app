package com.thekuzea.experimental.support.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.thekuzea.experimental.domain.enums.RestApiType;
import com.thekuzea.experimental.config.ApiProperties;

@Component
@RequiredArgsConstructor
public class ApiAccessorHelper {

    private final ApiProperties apiProperties;

    public String assembleUrl() {
        return String.format(
                "%s://%s:%d%s",
                apiProperties.getProtocol(),
                apiProperties.getHost(),
                apiProperties.getPort(),
                apiProperties.getPath()
        );
    }

    public String getApiSubPathByType(final RestApiType type) {
        return apiProperties.getSubPaths().get(type);
    }
}
