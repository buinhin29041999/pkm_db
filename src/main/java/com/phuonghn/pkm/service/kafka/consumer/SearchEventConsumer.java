package com.phuonghn.pkm.service.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SearchEventConsumer {

    private final Map<String, Integer> keywordCount = new ConcurrentHashMap<>();

    @KafkaListener(topics = "pokemon-search-events", groupId = "search-consumer-group")
    public void consume(String keyword) {
        keywordCount.merge(keyword.toLowerCase(), 1, Integer::sum);
        System.out.println("Keyword: " + keyword + " | Count: " + keywordCount.get(keyword.toLowerCase()));
    }

    public List<Map.Entry<String, Integer>> getTopKeywords(int top) {
        return keywordCount.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .limit(top).collect(Collectors.toList());
    }
}
