package com.know.wenda.domain;

import com.alibaba.fastjson.JSONObject;
import com.know.wenda.domain.base.Base;
import lombok.Data;


/**
 * 新鲜事
 * FeedDO
 *
 * @author shunhua
 */
@Data
public class FeedDO extends Base {
    private static final long serialVersionUID = 7091576386244052562L;
    /**
     * id
     */
    private Integer id;
    /**
     * 类型（如：关注的新鲜事，评论新鲜事）
     */
    private Integer type;
    /**
     * 用户id （新鲜事由人产生）
     */
    private Integer userId;
    /**
     * 新鲜事对应数据 以json形式保存
     */
    private String data;
    /**
     * JSONObject 辅助，为了方便读取data
     */
    private JSONObject dataJSON = null;

    /**
     * 给data设值，同时将data解析
     * @param data
     */
    public void setData(String data){
        this.data = data;
        this.dataJSON = JSONObject.parseObject(data);
    }

    /**
     * 可以直接通过json串中的key拿到对应的值,该方法的功能就是这
     * @param key
     * @return
     */
    public String get(String key){
        return dataJSON == null ? null:dataJSON.getString(key);
    }

}