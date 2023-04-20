package com.foo.prowler.app;

import com.foo.prowler.api.*;
import com.foo.prowler.config.AlertConfiguration;
import com.foo.prowler.dao.AlertDAO;
import com.foo.prowler.health.DatabaseHealthCheck;
import com.foo.prowler.resources.AlertResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertApplication extends Application<AlertConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlertApplication.class);

    private final HibernateBundle<AlertConfiguration> hibernateBundle =
            new HibernateBundle<AlertConfiguration>(
                    Alert.class,
                    RedactedContent.class,
                    SensitiveDataType.class
            ) {
                @Override
                public DataSourceFactory getDataSourceFactory(AlertConfiguration alertConfiguration) {
                    return alertConfiguration.getDataSourceFactory();
                }
            };

    public static void main(String[] args) throws Exception {
        new AlertApplication().run(args);
    }

    @Override
    public String getName() {
        return "Prowler Alert application";
    }

    @Override
    public void initialize(Bootstrap<AlertConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new MigrationsBundle<AlertConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(final AlertConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(AlertConfiguration configuration, Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        final AlertDAO alertDAO = new AlertDAO(hibernateBundle.getSessionFactory());

        LOGGER.info("Registering REST resources");
        environment.jersey().register(new AlertResource(alertDAO));

        LOGGER.info("Registering Application Health Check");
        environment.healthChecks().register("health",
                new DatabaseHealthCheck(jdbi, configuration.getDataSourceFactory().getValidationQuery()));

        /*
        LOGGER.info("Creating Message queue");
        final MessageQueueClient messageQueue = configuration.getMessageQueueFactory().build(environment);
        */
    }
}