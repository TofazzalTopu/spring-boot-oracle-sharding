package com.info.shard.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
public class ShardingDataSourceConfig {

    @Bean(name = "shard1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard1")
    public DataSource shard1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "shard2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard2")
    public DataSource shard2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(@Qualifier("shard1DataSource") DataSource shard1,
                                        @Qualifier("shard2DataSource") DataSource shard2) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("shard1", shard1);
        targetDataSources.put("shard2", shard2);

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(shard1);
        return routingDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("routingDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

