package yangqisheng.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqisheng
 * @date 2019-09-29 下午 13:35
 */
@Data
public class DemoMessage implements Serializable {

    private String messageId;
    private String content;
    private Integer messageCount;
}
