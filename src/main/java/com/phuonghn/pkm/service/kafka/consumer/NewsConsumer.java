package com.phuonghn.pkm.service.kafka.consumer;

import com.phuonghn.pkm.common.Constants;
import com.phuonghn.pkm.service.dto.NewsItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NewsConsumer {

    @Autowired
    private RedisTemplate<String, NewsItemDTO> redisTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final String REDIS_KEY = "pokemon-news";

    @KafkaListener(topics = Constants.KAFKA_TOPIC.PKM_NEWS, groupId = Constants.KAFKA_GROUP.PKM_NEWS)
    public void consume(NewsItemDTO newsItem) {
        redisTemplate.opsForList().leftPush(REDIS_KEY, newsItem);
        redisTemplate.opsForList().trim(REDIS_KEY, 0, 19);

        // Gửi tin mới qua WebSocket tới frontend
        messagingTemplate.convertAndSend("/topic/pokemon-news", newsItem);
    }
}
