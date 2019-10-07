package yangqisheng.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import yangqisheng.domain.DemoMessage;
import yangqisheng.producter.DemoProducter;

/**
 * @author yangqisheng
 * @date 2019-09-29 上午 10:08
 */
@Slf4j
@Component
public class DemoTask {

    @Autowired
    private DemoProducter demoProducter;
    private int count = 0;

    /**
     * 每秒产生一条消息
     */
    @Scheduled(fixedDelay = 1000)
    public void run() {
        count++;
        if (count < 20 && count % 5 == 0) {
            DemoMessage message = new DemoMessage();
            message.setMessageId(count + "");
            message.setContent("第" + count + "条");
            message.setMessageCount(0);
            demoProducter.sendMessage(message);
        }
    }
}
