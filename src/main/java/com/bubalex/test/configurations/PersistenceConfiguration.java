package com.bubalex.test.configurations;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * The Flyway configuration.
 */
@Configuration
public class PersistenceConfiguration {

    /**
     * The Flyway bean.
     */
    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .baselineOnMigrate(true)
                .locations(flywayLocations())
                .dataSource(dataSource)
                .load();
    }

    private String[] flywayLocations() {
        List<String> locations = new ArrayList<>();
        locations.add("classpath:/db/migration");
        return locations.toArray(new String[]{});
    }
}
