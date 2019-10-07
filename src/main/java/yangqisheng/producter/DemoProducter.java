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
 * @date 2019-09-29 上午 9:54
 */
@Slf4j
@Component
public class DemoProducter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${demo.rabbitmq.exchange}")
    private String demoExchange;
    @Value("${demo.rabbitmq.routing-key}")
    private String demoRoutingKey;

    /**
     * 发送消息
     *
     * @param content 消息内容
     */
    public void sendMessage(DemoMessage content) {
        log.info("发送消息：{}", content);
        rabbitTemplate.convertAndSend(demoExchange, demoRoutingKey, JSONObject.toJSONString(content), message -> {
            message.getMessageProperties().setContentEncoding("utf-8");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setMessageId(content.getMessageId());
            return message;
        });
    }
}
