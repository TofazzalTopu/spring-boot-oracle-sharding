package com.info.shard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootShardingApplicationTests {

	@Test
	void contextLoads() {
		int x = 2;
		assert x > 1 : "Success";
	}

}
