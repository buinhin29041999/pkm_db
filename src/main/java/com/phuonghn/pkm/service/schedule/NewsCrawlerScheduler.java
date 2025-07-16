package com.phuonghn.pkm.service.schedule;

import com.phuonghn.pkm.common.Constants;
import com.phuonghn.pkm.service.crawl.NewsCrawlerService;
import com.phuonghn.pkm.service.dto.NewsItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsCrawlerScheduler {

    @Autowired
    private NewsCrawlerService newsCrawlerService;

    @Autowired
    private KafkaTemplate<String, NewsItemDTO> newsKafkaTemplate;

    @Scheduled(fixedRate = 30000) // 1 phút crawl 1 lần
    public void crawlAndSend() {
        List<NewsItemDTO> newsItems = newsCrawlerService.crawlNews();
        newsItems.forEach(news -> newsKafkaTemplate.send(Constants.KAFKA_TOPIC.PKM_NEWS, news));
    }
}
