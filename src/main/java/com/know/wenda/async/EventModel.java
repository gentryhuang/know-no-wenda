package com.know.wenda.async;

import com.know.wenda.constant.EventType;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 事件发生的现场
 *
 * EventModel
 *
 * @author hlb
 */
@Data
public class EventModel implements Serializable {
    private static final long serialVersionUID = 7041558502536190156L;
    /**
     * 事件类型 如点赞
     */
    private EventType type;
    /**
     * 触发者
     */
    private int actorId;
    /**
     *  事件的对象 如 给谁点赞
     */
    private int entityType;
    private int entityId;
    /**
     *  事件对象相关的人
     */
    private int entityOwnerId;

    /**
     * 扩展字段 ，存储额外信息
     */
    private Map<String, String> exts = new HashMap<String, String>();

    public EventModel() {
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String key) {
        return exts.get(key);
    }


    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
