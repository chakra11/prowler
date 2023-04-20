package com.foo.prowler.module;

import com.foo.prowler.config.ScanConfiguration;
import com.foo.prowler.core.ScanTaskConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ScanModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
    }

    @Provides
    ScanTaskConfiguration getScannerConfiguration(ScanConfiguration configuration) {
        return configuration.getScannerConfiguration();
    }
}
