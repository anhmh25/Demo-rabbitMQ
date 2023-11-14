package com.example.demorabbitmq.config;

import static org.springframework.amqp.core.BindingBuilder.bind;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Consumer;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String TOPIC_EXCHANGE_NAME = "spring-boot-topic-exchange";

    public static final String DIRECT_EXCHANGE_NAME = "spring-boot-direct-exchange";

    public static final String DEFAULT_EXCHANGE_NAME = "";

    public static final String FANOUT_EXCHANGE_NAME = "spring-boot-fanout-exchange";

    public static final String HEADERS_EXCHANGE_NAME = "spring-boot-headers-exchange";

    public static final String FANOUT_QUEUE_1_NAME = "fanout.queue-1";

    public static final String FANOUT_QUEUE_2_NAME = "fanout.queue-2";

    public static final String TOPIC_QUEUE_1_NAME = "topic.queue-1";

    public static final String TOPIC_QUEUE_2_NAME = "topic.queue-2";

    public static final String HEADERS_QUEUE_NAME = "header.queue";

    public int headerCount = 0;
    public int topicCount = 0;
    public int fanoutCount = 0;
    public int directCount = 0;

    @Bean
    public Declarables fanoutBindings() {
        Queue fanoutQueue1 = new Queue(FANOUT_QUEUE_1_NAME, false);
        Queue fanoutQueue2 = new Queue(FANOUT_QUEUE_2_NAME, false);
        FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_EXCHANGE_NAME);
        return new Declarables(
                fanoutQueue1,
                fanoutQueue2,
                fanoutExchange,
                bind(fanoutQueue1).to(fanoutExchange),
                bind(fanoutQueue2).to(fanoutExchange));
    }

    @Bean
    public Declarables topicBindings() {
        Queue topicQueue1 = new Queue(TOPIC_QUEUE_1_NAME, false);
        Queue topicQueue2 = new Queue(TOPIC_QUEUE_2_NAME, false);
        TopicExchange topicExchange = new TopicExchange(TOPIC_EXCHANGE_NAME);
        return new Declarables(
                topicQueue1,
                topicQueue2,
                topicExchange,
                bind(topicQueue1).to(topicExchange).with("*.important.*"),
                bind(topicQueue2).to(topicExchange).with("#.error")
        );
    }

    @Bean
    public Declarables headerBindings() {
        Queue headerQueue = new Queue(HEADERS_QUEUE_NAME, false);
        HeadersExchange headersExchange = new HeadersExchange(HEADERS_EXCHANGE_NAME);
        return new Declarables(
                headerQueue,
                headersExchange,
                bind(headerQueue).to(headersExchange).where("level").matches("error")
        );
    }

    @Bean
    public Declarables directBingdings() {
        Queue directQueue1 = new Queue(TOPIC_QUEUE_1_NAME, false);
        Queue directQueue2 = new Queue(TOPIC_QUEUE_2_NAME, false);
        DirectExchange directExchange = new DirectExchange(DIRECT_EXCHANGE_NAME);
        return new Declarables(
                directExchange,
                bind(directQueue1).to(directExchange).with("direct.exchange-1"),
                bind(directQueue2).to(directExchange).with("direct.exchange-2")
        );
    }

    @RabbitListener(queues = {FANOUT_QUEUE_1_NAME})
    public void receiveMessageFanout1(String message) throws InterruptedException {
        System.out.println(String.format("[%s] [%s] Received message: %s", FANOUT_EXCHANGE_NAME, FANOUT_QUEUE_1_NAME, message));
        fanoutCount++;
        if (fanoutCount == 30) {
            fanoutCount = 0;
            Thread.sleep(2000);
        }
    }

    @RabbitListener(queues = {FANOUT_QUEUE_2_NAME})
    public void receiveMessageFanout2(String message) throws InterruptedException {
        System.out.println(String.format("[%s] [%s] Received message: %s", FANOUT_EXCHANGE_NAME, FANOUT_QUEUE_2_NAME, message));
        fanoutCount++;
        if (fanoutCount == 30) {
            fanoutCount = 0;
            Thread.sleep(2000);
        }
    }

    @RabbitListener(queues = {TOPIC_QUEUE_1_NAME})
    public void receiveMessageTopic1(String message) throws InterruptedException {
        System.out.println(String.format("[%s] [%s] Received message: %s", TOPIC_EXCHANGE_NAME, TOPIC_QUEUE_1_NAME, message));
        topicCount++;
        if (topicCount == 30) {
            topicCount = 0;
            Thread.sleep(2000);
        }
    }

    @RabbitListener(queues = {TOPIC_QUEUE_2_NAME})
    public void receiveMessageTopic2(String message) throws InterruptedException {
        System.out.println(String.format("[%s] [%s] Received message: %s", TOPIC_EXCHANGE_NAME, TOPIC_QUEUE_2_NAME, message));
        topicCount++;
        if (topicCount == 30) {
            topicCount = 0;
            Thread.sleep(2000);
        }
    }

    @RabbitListener(queues = {HEADERS_QUEUE_NAME})
    public void receiveMessageHeader1(byte[] data) throws InterruptedException {
        System.out.println(String.format("[%s] Received message: %s", HEADERS_EXCHANGE_NAME, new String(data)));
        headerCount++;
        if (headerCount == 30) {
            headerCount = 0;
            Thread.sleep(2000);
        }
    }

//    @Bean
//    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames("fanout.queue-1", "fanout.queue-2", "topic.queue-1", "topic.queue-2");
//        container.setConcurrentConsumers(1); // Số lượng consumers cần xử lý cùng một lúc
//        container.setMaxConcurrentConsumers(1); // Số lượng consumers tối đa
//        container.setReceiveTimeout(10000); // Thời gian chờ (milliseconds) trước khi ngủ giữa các lần xử lý
//        container.setIdleEventInterval(10000); // Số lượng request cần xử lý trong 1 giây
//        return container;
//    }

//    @Bean
//    public MessageListenerAdapter listenerAdapter(Consumer consumer) {
//        return new MessageListenerAdapter(consumer, "receiveMessage");
//    }

}
