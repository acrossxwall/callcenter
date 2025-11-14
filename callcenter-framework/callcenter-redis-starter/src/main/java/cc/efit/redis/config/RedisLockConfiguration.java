package cc.efit.redis.config;

import cc.efit.redis.utils.RedisLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = RedisConfiguration.class)
public class RedisLockConfiguration {
    @Bean
    public RedisLock redisLock(RedissonClient redissonClient) {
        return new RedisLock(redissonClient);
    }
}
