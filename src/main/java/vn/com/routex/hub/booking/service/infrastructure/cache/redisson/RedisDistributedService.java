package vn.com.routex.hub.booking.service.infrastructure.cache.redisson;

import java.util.List;

public interface RedisDistributedService {

    RedisDistributedLocker getMultiLock(List<String> lockKeys);
    RedisDistributedLocker getDistributedLock(String lockKey);
}
