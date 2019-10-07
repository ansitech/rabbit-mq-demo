package yangqisheng.producter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yangqisheng.domain.DemoMessage;

/**
 * @author yangqisheng
 * @date 2019_09_29 上午 10:30
 */
@Slf4j
@Component
public class DemoDelay300Producter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${demo.rabbitmq.delay-exchange}")
    private String demoDelayExchange;
    @Value("${demo.rabbitmq.delay-routing-key}")
    private String demoDelayRoutingKey;

    /**
     * 发送延时消息
     *
     * @param content 消息内容
     */
    public void sendDelayMessage(DemoMessage content) {
        log.info("再次发送消息：{}", content.getContent());
        rabbitTemplate.convertAndSend(demoDelayExchange + "_300", demoDelayRoutingKey + "_300",
                JSONObject.toJSONString(content), message -> {
                    message.getMessageProperties().setContentEncoding("utf-8");
                    message.getMessageProperties().setMessageId(content.getMessageId());
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    String expiration = "300000";
                    log.info("消息{}第{}次重新发送，延时{}ms", content.getMessageId(), content.getMessageCount(), expiration);
                    //根据发送次数，计算下次发送时间
                    message.getMessageProperties().setExpiration(expiration);
                    return message;
                });
    }
}