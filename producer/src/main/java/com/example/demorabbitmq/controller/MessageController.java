package com.example.demorabbitmq.controller;
import com.example.demorabbitmq.config.RabbitMQConfig;
import com.example.demorabbitmq.model.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageBuilderSupport;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/message/header-exchange")
    public ResponseEntity<String> sendMessageHeader(@RequestBody final Message message) {
        org.springframework.amqp.core.Message headerMessage = MessageBuilder.withBody(
                ("header error: " + message.getMessage()).getBytes())
                .setHeader("level", "error")
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.HEADERS_EXCHANGE_NAME, "", headerMessage);

        String json = "{\"status\": \"Message processing ...\"}";
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    @PostMapping("/message/fanout-exchange")
    public ResponseEntity<String> sendMessageFanout(@RequestBody final Message message) {
        org.springframework.amqp.core.Message headerMessage = MessageBuilder.withBody(
                ("header error: " + message.getMessage()).getBytes())
                .setHeader("level", "error")
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_NAME, "", "fanout: " + message.getMessage());

        String json = "{\"status\": \"Message processing ...\"}";
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    @PostMapping("/message/default-exchange")
    public ResponseEntity<String> sendMessageDefault(@RequestBody final Message message) {
        org.springframework.amqp.core.Message headerMessage = MessageBuilder.withBody(
                ("header error: " + message.getMessage()).getBytes())
                .setHeader("level", "error")
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.DEFAULT_EXCHANGE_NAME, RabbitMQConfig.HEADERS_QUEUE_NAME,
                ("default: " + message.getMessage()).getBytes());

        String json = "{\"status\": \"Message processing ...\"}";
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    @PostMapping("/message/topic-exchange")
    public ResponseEntity<String> sendMessageTopic(@RequestBody final Message message) {
        MessageBuilderSupport<org.springframework.amqp.core.Message> headerMessage = MessageBuilder.withBody(
                ("header error: " + message.getMessage()).getBytes())
                .setHeader("level", "error");

        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, "msg.important.warn",
                "topic important warn: " + message.getMessage());

        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_NAME, "msg.error",
                "topic important error: " + message.getMessage());

        String json = "{\"status\": \"Message processing ...\"}";
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    @PostMapping("/message/direct-exchange")
    public ResponseEntity<String> sendMessageDirect(@RequestBody final Message message) {
        org.springframework.amqp.core.Message headerMessage = MessageBuilder.withBody(
                ("header error: " + message.getMessage()).getBytes())
                .setHeader("level", "error")
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE_NAME, "direct.exchange-1",
                "direct-1: " + message.getMessage());

        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE_NAME, "direct.exchange-2",
                "direct-2: " + message.getMessage());
        
        String json = "{\"status\": \"Message processing ...\"}";
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }
}
