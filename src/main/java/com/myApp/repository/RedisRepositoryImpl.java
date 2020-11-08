package com.myApp.repository;

import com.myApp.config.RedisConfig;
import com.myApp.domain.NewsFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

   @Autowired
    RedisConfig redisConfig;

    @Override
    public void saveAll(Map<String, NewsFeed> newsFeedMap) {
        redisConfig.getRedisTemplate().opsForHash().putAll("NewsFeed", newsFeedMap);
    }

    @Override
    public NewsFeed findById(String userId) {
        return (NewsFeed) redisConfig.getRedisTemplate().opsForHash().get("NewsFeed", userId);
    }
}
