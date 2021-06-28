package com.example.sharding;

import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.Map;

/**
 * 通常基于订单号 时间 用户ID  设计上可以把用户ID的尾号放在订单号中
 * <p>
 * 具体和标准分片类似
 *
 * @author hdf
 */
public class OrderShardingAlgorithm implements ComplexKeysShardingAlgorithm<String> {
    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<String> complexKeysShardingValue) {
        Map<String, Collection<String>> columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public String getType() {
        return null;
    }
}
