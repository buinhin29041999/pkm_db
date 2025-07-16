package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.service.dto.NewsItemDTO;
import com.phuonghn.pkm.service.kafka.consumer.NewsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsConsumer newsConsumer;

    @GetMapping
    public List<NewsItemDTO> getLatestNews() {
//        return newsConsumer.getLatestNews();
        return null;
    }
}
