package com.foo.prowler.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foo.prowler.api.SensitiveTypeName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScanTaskConfiguration {
    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("application")
    private String application;

    @JsonProperty("location")
    private String location;

    @JsonProperty("sensitiveDataTypes")
    private List<SensitiveTypeName> sensitiveDataTypes;
}
