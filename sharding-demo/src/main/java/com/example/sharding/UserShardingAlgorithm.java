package com.example.sharding;

import com.google.common.base.Preconditions;
import org.apache.shardingsphere.sharding.algorithm.sharding.range.AbstractRangeShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * 业界通常百库十表
 * <p>
 * 例子以10位用户号
 *
 * <p>
 * 自定义分片算法
 *
 * @author hdf
 */
public class UserShardingAlgorithm implements StandardShardingAlgorithm<String> {

    private static final Logger logger = LoggerFactory.getLogger(UserShardingAlgorithm.class);

    private static final String SEGMENT = "segment";
    private int[] segmentIndex;
    private Properties props = new Properties();

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        String value = shardingValue.getValue();
        for (String each : availableTargetNames) {
            if (each.endsWith(value.substring(segmentIndex[0], segmentIndex[1]))) {
                return each;
            }
        }
        return null;
    }

    /**
     * 每个算法需要定义 大于小于，between 的场景 不需要可以直接报错，如果需要实现{@link AbstractRangeShardingAlgorithm}
     *
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<String> shardingValue) {
        throw new RuntimeException("not support");
    }

    @Override
    public void init() {
        Preconditions.checkArgument(props.containsKey(SEGMENT), "segment cannot be null.");
        String[] segmentSplit = props.getProperty(SEGMENT).split(",");
        Preconditions.checkArgument(segmentSplit.length == 2, "segment must contains one comma");
        segmentIndex = Arrays.stream(segmentSplit).mapToInt(Integer::parseInt).toArray();
        logger.info("user UserShardingAlgorithm segmentIndex {},{}", segmentIndex[0], segmentIndex[1]);
    }

    @Override
    public String getType() {
        return "USER_ID";
    }

    @Override
    public Properties getProps() {
        return props;
    }

    @Override
    public void setProps(Properties properties) {
        this.props = properties;
    }

}
