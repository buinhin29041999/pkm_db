package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.service.kafka.consumer.SearchEventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    @Autowired
    private SearchEventConsumer consumer;

    @GetMapping("/top-searches")
    public List<Map.Entry<String, Integer>> getTopSearches() {
        return consumer.getTopKeywords(5);
    }
}

