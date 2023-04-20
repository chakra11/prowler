package com.foo.prowler.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foo.prowler.core.ScanTaskConfiguration;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ScanConfiguration extends Configuration {
    private static final String SCANNER = "scanner";

    @Valid
    @NotNull
    private ScanTaskConfiguration scanTaskConfiguration;

    @JsonProperty(SCANNER)
    public ScanTaskConfiguration getScannerConfiguration() {
        return scanTaskConfiguration;
    }

    @JsonProperty(SCANNER)
    public void setScannerConfiguration(final ScanTaskConfiguration scanTaskConfiguration) {
        this.scanTaskConfiguration = scanTaskConfiguration;
    }
}
