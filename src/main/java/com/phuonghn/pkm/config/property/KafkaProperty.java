package com.phuonghn.pkm.config.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class KafkaProperty {

    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.port}")
    private String kafkaPort;
}
