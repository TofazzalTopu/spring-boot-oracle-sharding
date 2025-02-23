package com.info.shard.config.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setShard(String shard) {
        contextHolder.set(shard);
    }

    public static Object getCurrentShard() {
        return contextHolder.get();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove(); // Ensures thread-local data is cleared
    }
}
