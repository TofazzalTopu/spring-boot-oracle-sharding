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

@Configuration
public class ShardingDataSourceConfig {

    @Bean(name = "shardOneDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard1")
    public DataSource shardOneDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "shardTwoDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard2")
    public DataSource shardTwoDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(@Qualifier("shardOneDataSource") DataSource shardOne,
                                        @Qualifier("shardTwoDataSource") DataSource shardTwo) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("shardOne", shardOne);
        targetDataSources.put("shardTwo", shardTwo);

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(shardOne);
        return routingDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("routingDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

