package com.know.wenda.async;

import com.know.wenda.constant.EventType;

import java.util.List;


/**
 * EventHandler
 *
 * @author hlb
 */
public interface EventHandler {

    /**
     * 要处理的Event
     * @param model
     */
    void doHandle(EventModel model);

    /**
     * 关心哪些Event
     * @return
     */
    List<EventType> getSupportEventTypes();
}
