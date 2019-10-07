package yangqisheng.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangqisheng
 * @date 2019-09-29 上午 9:33
 */
@Configuration
public class RabbitConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${demo.rabbitmq.exchange}")
    private String demoExchange;
    @Value("${demo.rabbitmq.queue}")
    private String demoQueue;
    @Value("${demo.rabbitmq.routing-key}")
    private String demoRoutingKey;

    /**
     * 生成交换机
     *
     * @return 交换机对象
     */
    @Bean("demoExchange")
    public Exchange demoExchange() {
        return ExchangeBuilder.directExchange(demoExchange).durable(true).build();
    }

    /**
     * 生成队列
     *
     * @return 队列
     */
    @Bean("demoQueue")
    public Queue demoQueue() {
        return QueueBuilder.durable(demoQueue).build();
    }

    /**
     * 交换机绑定队列和路由
     *
     * @return 绑定对象
     */
    @Bean
    public Binding demoBinding() {
        return new Binding(demoQueue, Binding.DestinationType.QUEUE, demoExchange, demoRoutingKey, null);
    }
}
