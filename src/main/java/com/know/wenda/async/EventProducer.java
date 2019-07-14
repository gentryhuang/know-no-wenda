package com.know.wenda.async;

import com.alibaba.fastjson.JSONObject;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import com.know.wenda.util.JedisAdapter;
import com.know.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 *使用redis作为消息队列的功能 （redis的list消息队列不支持优先级队列）
 * （1） lpush 保证进入队列
 * （2） brpop 保证从尾部取出
 *
 * EventProducer
 * 把事件推进redis的消息队列中
 *
 * @author hlb
 */
@Component
public class EventProducer {

    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 把EventModel 发出去 即放入redis的列表中
     * @param eventModel
     * @return
     */
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            System.out.println("发起事件了："+json);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER,KnwLoggerMarkers.BUSINESS_INFO,"发布消息异常",eventModel);
            return false;
        }
    }
}
