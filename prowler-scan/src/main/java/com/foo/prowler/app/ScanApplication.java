package com.foo.prowler.app;

import com.foo.prowler.config.ScanConfiguration;
import com.foo.prowler.core.ScanExecutor;
import com.foo.prowler.core.ScanTaskConfiguration;
import com.foo.prowler.resources.ScanResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;

public class ScanApplication extends Application<ScanConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanApplication.class);
    private static final String nameFormat = "scan-%d";

    public static void main(String[] args) throws Exception {
        new ScanApplication().run(args);
    }

    @Override
    public String getName() {
        return "Prowler Scan application";
    }

    @Override
    public void initialize(Bootstrap<ScanConfiguration> bootstrap) {
        /*
        bootstrap.addBundle(
                GuiceBundle.builder()
                        .enableAutoConfig(getClass().getPackage().getName())
                        .modules(new ScanModule())
                        .build()
        );
        */
    }

    @Override
    public void run(ScanConfiguration configuration, Environment environment) throws Exception {
        LOGGER.info("Start managed scan executor");
        final ScheduledExecutorService scheduledExecutorService = environment.lifecycle()
                .scheduledExecutorService(nameFormat)
                .build();
        final ScanTaskConfiguration scanTaskConfiguration = configuration.getScannerConfiguration();
        final ScanExecutor scanExecutor = new ScanExecutor(scheduledExecutorService, scanTaskConfiguration);
        environment.lifecycle().manage(scanExecutor);

        LOGGER.info("Registering REST resources");
        environment.jersey().register(new ScanResource(scanExecutor));
    }

    /*
    @Provides
    @Singleton
    protected ScheduledExecutorService provideScanExecutorService(Environment environment) {
        return environment.lifecycle()
                .scheduledExecutorService(nameFormat)
                .build();
    }
    */
}