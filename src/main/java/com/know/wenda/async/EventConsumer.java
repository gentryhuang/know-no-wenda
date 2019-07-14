package com.know.wenda.async;

import com.alibaba.fastjson.JSON;
import com.know.wenda.constant.EventType;
import com.know.wenda.util.JedisAdapter;
import com.know.wenda.util.RedisKeyUtil;
import com.know.wenda.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * EventConsumer
 * 从Redis队列中取出并处理事件
 *
 * @author hlb
 */
@Component
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    private JedisAdapter jedisAdapter;
    /**
     * 维护事件与对应的事件处理关系
     */
    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    /**
     * Spring的上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 在Bean实例化后，把所有的事件处理拿到
         * 1. getBeansOfType的到的Map，key是Bean的名称，value是bean的实例
         * 2. getBeansOfType方法有两种类型的重载：
         *    getBeansOfType(Class) 获取某一类的所有bean
         *    getBeansOfType(Class,boolean,boolean) ,后面的两个布尔值，第一个代表是否也包含原型（Class祖先）bean或者只是singletons（包含FactoryBean生成的），
         *       第二个表示是否立即实例化懒加载或者由FactoryBean生成的Bean以保证依赖关系。
         */

        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                // 找到处理消息的Handler，然后把它加入到config的Map中
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.getThreadPool();
        threadPoolExecutor.execute(() -> {
            while (true) {
                // 只处理event_queue 的key 事件
                String key = RedisKeyUtil.getEventQueueKey();
                // 从列表中找出事件，0 表示没有的话就等待
                List<String> events = jedisAdapter.brpop(0, key);
                // 第一个可能是key,如果是就把它过滤掉，这是因为brpop返回值的原因
                events.remove(key);
                // 找到后，再找对应的事件处理取处理它
                events.stream().forEach(event -> {
                    // 取出事件
                    EventModel eventModel = JSON.parseObject(event, EventModel.class);
                    if (!config.containsKey(eventModel.getType())) {
                        logger.error("不能识别的事件");
                    }
                    // 找到对应的EventHandler链处理事件，责任链模式
                    List<EventHandler> handlers = config.get(eventModel.getType());
                    handlers.stream().forEach(eventHandler -> {
                        eventHandler.doHandle(eventModel);
                    });
                });
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
