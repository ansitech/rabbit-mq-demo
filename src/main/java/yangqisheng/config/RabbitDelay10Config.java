package yangqisheng.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangqisheng
 * @date 2019-09-29 上午 10:39
 */
@Configuration
public class RabbitDelay10Config {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${demo.rabbitmq.delay-exchange}")
    private String demoDelayExchange;
    @Value("${demo.rabbitmq.queue}")
    private String demoQueue;
    @Value("${demo.rabbitmq.delay-queue}")
    private String demoDelayQueue;
    @Value("${demo.rabbitmq.delay-routing-key}")
    private String demoDelayRoutingKey;
    @Value("${demo.rabbitmq.routing-key}")
    private String demoRoutingKey;

    /**
     * 生成延时交换机
     *
     * @return 延时交换机
     */
    @Bean("demoDelayExchange_10")
    public Exchange demoDelayExchange_10() {
        return ExchangeBuilder.directExchange(demoDelayExchange + "_10").durable(true).build();
    }

    /**
     * 生成延时队列
     *
     * @return 延时队列
     */
    @Bean("demoDelayQueue_10")
    public Queue demoDelayQueue_10() {
        Map<String, Object> args = new HashMap<>(4);
        //指定DLX
        args.put("x-dead-letter-exchange", demoDelayExchange + "_10");
        //指定DLK
        args.put("x-dead-letter-routing-key", demoRoutingKey);
        return QueueBuilder.durable(demoDelayQueue + "_10").withArguments(args).build();
    }

    /**
     * 交换机绑定队列和路由
     *
     * @return 延时绑定对象
     */
    @Bean("demoDelayBinding_10")
    public Binding demoDelayBinding_10() {
        return new Binding(demoDelayQueue + "_10", Binding.DestinationType.QUEUE, demoDelayExchange + "_10", demoDelayRoutingKey + "_10", null);
    }

    /**
     * 重新绑定
     *
     * @return 重新绑定对象
     */
    @Bean("demoReBinding_10")
    public Binding demoReBinding_10() {
        return new Binding(demoQueue, Binding.DestinationType.QUEUE, demoDelayExchange + "_10", demoRoutingKey, null);
    }
}
