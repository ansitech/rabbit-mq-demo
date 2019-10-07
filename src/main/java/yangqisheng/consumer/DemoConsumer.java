package yangqisheng.consumer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import yangqisheng.domain.DemoMessage;
import yangqisheng.producter.*;

import java.util.Map;

/**
 * @author yangqisheng
 * @date 2019-09-29 上午 9:59
 */
@Slf4j
@Component
public class DemoConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DemoDelayProducter demoDelayProducter;
    @Autowired
    private DemoDelay10Producter demoDelay10Producter;
    @Autowired
    private DemoDelay20Producter demoDelay20Producter;
    @Autowired
    private DemoDelay30Producter demoDelay30Producter;
    @Autowired
    private DemoDelay60Producter demoDelay60Producter;
    @Autowired
    private DemoDelay300Producter demoDelay300Producter;
    @Autowired
    private DemoErrorProducter demoErrorProducter;

    @RabbitListener(queues = "${demo.rabbitmq.queue}")
    public void receiveMessage(@Headers Map<String, Object> headers, Message message) {
        DemoMessage content = null;
        try {
            content = JSONObject.parseObject(new String(message.getBody()), DemoMessage.class);
            log.info("接收消息：messageId={},content={},第{}次",
                    content.getContent(), message.getMessageProperties().getMessageId(), content.getMessageCount());
            throw new Exception("消费异常");
        } catch (Exception e) {
            if (content == null) {
                demoErrorProducter.sendErrorMessage(message.getBody());
            }
            content.setMessageCount(content.getMessageCount() + 1);
            switch (content.getMessageCount()) {
                case 1:
                case 2:
                case 3:
                    demoDelayProducter.sendDelayMessage(content);
                    break;
                case 4:
                case 5:
                    demoDelay10Producter.sendDelayMessage(content);
                    break;
                case 6:
                case 7:
                    demoDelay20Producter.sendDelayMessage(content);
                    break;
                case 8:
                    demoDelay30Producter.sendDelayMessage(content);
                    break;
                case 9:
                    demoDelay60Producter.sendDelayMessage(content);
                    break;
                case 10:
                    demoDelay300Producter.sendDelayMessage(content);
                    break;
                default:
                    demoErrorProducter.sendErrorMessage(content);
            }
        }
    }
}
