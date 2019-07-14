package com.know.wenda.domain;

import com.know.wenda.domain.base.Base;
import lombok.Data;

/**
 * MessageDO
 *
 * @author shunhua
 */
@Data
public class MessageDO extends Base {
    private static final long serialVersionUID = -7582093176668985438L;
    /**
     * 消息id
     */
    private Integer id;
    /**
     * 发送方id
     */
    private Integer fromId;
    /**
     * 接收方id
     */
    private Integer toId;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否已读
     */
    private Integer hasRead;
    /**
     * 双方公用的会话id
     */
    private String conversationId;

    /**
     * A->B 和B->A 是一样的， conversationId是唯一的
     * @return
     */
    public String getConversationId() {
        if (fromId < toId) {
            return String.format("%d_%d", fromId, toId);
        } else {
            return String.format("%d_%d", toId, fromId);
        }
    }


}