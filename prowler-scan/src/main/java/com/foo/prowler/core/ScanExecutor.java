package com.foo.prowler.core;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//@Singleton
public class ScanExecutor implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanExecutor.class);
    private final ScheduledExecutorService scheduledExecutorService;
    private final ScanTaskConfiguration scanTaskConfiguration;

    private final Runnable scanTask;

    private ScheduledFuture<?> scheduledFuture;

    //@Inject
    public ScanExecutor(
            final ScheduledExecutorService scheduledExecutorService,
            final ScanTaskConfiguration scanTaskConfiguration) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.scanTaskConfiguration = scanTaskConfiguration;

        this.scanTask = new ScanTask(scanTaskConfiguration);
    }

    @Override
    public void start() {
        LOGGER.info("Scan executor started");
        // TODO: Get delay from config
        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(scanTask, 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        LOGGER.info("Scan executor stopped");
        scheduledFuture.cancel(true);
    }
}
