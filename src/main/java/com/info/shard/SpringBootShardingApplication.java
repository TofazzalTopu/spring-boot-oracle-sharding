package com.info.shard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringBootShardingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootShardingApplication.class, args);
	}

}
