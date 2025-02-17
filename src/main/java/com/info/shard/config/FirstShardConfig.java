package com.info.shard.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.info.shard.repository.shard1", entityManagerFactoryRef = "shard1EntityManagerFactory", transactionManagerRef = "shard1TransactionManager")
public class FirstShardConfig {

    @Primary
    @Bean(name = "shard1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.shard1")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "shard1EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("shard1DataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource).packages("com.info.shard.entity") // Entity package
                .persistenceUnit("shard1")
                .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.OracleDialect")) // Explicitly set dialect
                .build();
    }

    @Primary
    @Bean(name = "shard1TransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("shard1EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

//    @Primary
//    @Bean
//    @ConfigurationProperties("spring.datasource.shard1")
//    public HikariDataSource shard1DataSource() {
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//    }

}

