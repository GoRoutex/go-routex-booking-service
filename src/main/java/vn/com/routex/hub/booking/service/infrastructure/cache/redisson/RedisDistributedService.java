package vn.com.routex.hub.booking.service.infrastructure.cache.redisson;

public interface RedisDistributedService {

    RedisDistributedLocker getDistributedLock(String lockKey);
}
