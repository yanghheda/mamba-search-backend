package com.rc.mambasaerchbackend.example;

import com.rc.mambasaerchbackend.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * Redis 使用示例
 */
@Tag(name = "示例-Redis", description = "Redis 操作示例接口")
@RestController
@RequestMapping("/example/redis")
public class RedisExample {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisExample(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Operation(summary = "写入缓存")
    @PostMapping("/set")
    public BaseResponse<Void> set(@RequestParam String key,
                                   @RequestParam String value,
                                   @RequestParam(defaultValue = "300") long ttl) {
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
        return BaseResponse.success();
    }

    @Operation(summary = "读取缓存")
    @GetMapping("/get")
    public BaseResponse<Object> get(@RequestParam String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return BaseResponse.success(value);
    }

    @Operation(summary = "判断 key 是否存在")
    @GetMapping("/exists")
    public BaseResponse<Boolean> exists(@RequestParam String key) {
        return BaseResponse.success(redisTemplate.hasKey(key));
    }

    @Operation(summary = "删除缓存")
    @DeleteMapping("/delete")
    public BaseResponse<Void> delete(@RequestParam String key) {
        redisTemplate.delete(key);
        return BaseResponse.success();
    }

    @Operation(summary = "递增")
    @PostMapping("/increment")
    public BaseResponse<Long> increment(@RequestParam String key,
                                         @RequestParam(defaultValue = "1") long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return BaseResponse.success(result);
    }

    @Operation(summary = "获取剩余过期时间(秒)")
    @GetMapping("/ttl")
    public BaseResponse<Long> ttl(@RequestParam String key) {
        return BaseResponse.success(redisTemplate.getExpire(key, TimeUnit.SECONDS));
    }
}
