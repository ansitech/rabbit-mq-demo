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
public class DemoErrorProducter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${demo.rabbitmq.error-exchange}")
    private String demoErrorExchange;
    @Value("${demo.rabbitmq.error-routing-key}")
    private String demoErrorRoutingKey;

    /**
     * 发送消息
     *
     * @param content 消息内容
     */
    public void sendErrorMessage(DemoMessage content) {
        log.info("记录错误消息：{}", content.getContent());
        rabbitTemplate.convertAndSend(demoErrorExchange, demoErrorRoutingKey, JSONObject.toJSONString(content), message -> {
            message.getMessageProperties().setContentEncoding("utf-8");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setMessageId(content.getMessageId());
            return message;
        });
    }

    /**
     * 发送消息
     *
     * @param content 消息内容
     */
    public void sendErrorMessage(byte[] content) {
        log.info("记录异常消息：内容长度{}", content.length);
        rabbitTemplate.convertAndSend(demoErrorExchange, demoErrorRoutingKey, content, message -> {
            message.getMessageProperties().setContentEncoding("utf-8");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });
    }
}
