package com.info.shard.config;

import com.info.shard.config.jdbc.RoutingDataSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;

class RoutingDataSourceTest {

    @AfterEach
    void tearDown() {
        RoutingDataSource.clear(); // Ensure cleanup after each test
    }

    @Test
    void testSetShardAndDetermineLookupKey() {
        RoutingDataSource.setShard("shard_1");
        assertNotNull(RoutingDataSource.getCurrentShard(), "Shard should not be null before clear");
        assertEquals("shard_1", RoutingDataSource.getCurrentShard(), "Shard should be shard_1");
    }

    @Test
    void testClearShard() {
        RoutingDataSource.setShard("shard_2");
        assertNotNull(RoutingDataSource.getCurrentShard(), "Shard should not be null before clear");
        RoutingDataSource.clear();
        assertNull(RoutingDataSource.getCurrentShard(), "Shard should be null after clearing");
    }

    @Test
    void testDefaultLookupKeyIsNull() {
        assertNull(RoutingDataSource.getCurrentShard(), "Default lookup key should be null");
    }

    @Test
    void testClearWithoutSettingValue() {
        // Ensure default state
        assertNull(RoutingDataSource.getCurrentShard(), "Initial shard value should be null");
        // Call clear directly
        RoutingDataSource.clear();
        // Ensure it is still null
        assertNull(RoutingDataSource.getCurrentShard(), "Shard should remain null after calling clear");
    }
}


