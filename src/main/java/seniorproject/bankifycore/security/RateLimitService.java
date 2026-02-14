package seniorproject.bankifycore.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final StringRedisTemplate redis;

    public boolean allow(String key, long limitPerMinute) {
        long minuteBucket = Instant.now().getEpochSecond() / 60;
        String redisKey = key + ":" + minuteBucket;

        Long count = redis.opsForValue().increment(redisKey);
        if (count != null && count == 1L) {
            redis.expire(redisKey, 70, TimeUnit.SECONDS); // slightly > 60s
        }
        return count != null && count <= limitPerMinute;
    }
}
