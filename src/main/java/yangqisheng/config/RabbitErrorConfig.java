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
public class RabbitErrorConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${demo.rabbitmq.error-exchange}")
    private String demoErrorExchange;
    @Value("${demo.rabbitmq.error-queue}")
    private String demoErrorQueue;
    @Value("${demo.rabbitmq.error-routing-key}")
    private String demoErrorRoutingKey;

    /**
     * 生成交换机
     *
     * @return 交换机对象
     */
    @Bean("demoErrorExchange")
    public Exchange demoErrorExchange() {
        return ExchangeBuilder.directExchange(demoErrorExchange).durable(true).build();
    }

    /**
     * 生成队列
     *
     * @return 队列
     */
    @Bean("demoErrorQueue")
    public Queue demoErrorQueue() {
        return QueueBuilder.durable(demoErrorQueue).build();
    }

    /**
     * 交换机绑定队列和路由
     *
     * @return 绑定对象
     */
    @Bean
    public Binding demoErrorBinding() {
        return new Binding(demoErrorQueue, Binding.DestinationType.QUEUE, demoErrorExchange, demoErrorRoutingKey, null);
    }
}
