package com.info.shard.config.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.info.shard.repository.shard2",
        entityManagerFactoryRef = "shard2EntityManagerFactory",
        transactionManagerRef = "shard2TransactionManager"
)
public class SecondShardConfig {

    @Bean(name = "shard2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard2")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "shard2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("shard2DataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.info.shard.entity") // Entity package
                .persistenceUnit("shard2")
                .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.OracleDialect")) // Explicitly set dialect
                .build();
    }

    @Bean(name = "shard2TransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("shard2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
