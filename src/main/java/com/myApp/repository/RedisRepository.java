package com.myApp.repository;

import com.myApp.domain.NewsFeed;

import java.util.Map;

public interface RedisRepository {
    void saveAll(Map<String, NewsFeed> newsFeed);

    NewsFeed findById(String userId);
}
