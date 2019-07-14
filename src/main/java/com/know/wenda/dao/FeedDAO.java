package com.know.wenda.dao;

import com.know.wenda.dao.mapper.FeedMapper;
import com.know.wenda.domain.FeedDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * FeedDAO
 *
 * @author shunhua
 */
@Repository
public class FeedDAO {

    @Resource
    private FeedMapper feedMapper;

    /**
     * 新增
     *
     * @param feedDO
     * @return
     */
    public int insert(FeedDO feedDO) {
        return feedMapper.insert(feedDO);
    }

    /**
     * 根据PK查询  推模式
     *
     * @param id
     * @return
     */
    public FeedDO get(int id) {
        return feedMapper.get(id);
    }

    /**
     * 多条件查询，拉模式
     *
     * @param maxId  可以增量的更新
     * @param userIds   从关注的好友中找（登录时有效）
     * @param count   分页参数
     * @return
     */
    public List<FeedDO> selectUserFeeds(int maxId, List<Integer> userIds, int count) {
        List<FeedDO> feedDOS = feedMapper.selectUserFeeds(maxId, userIds, count);
        return feedDOS;
    }

    /**
     * 获取当前用户关心的新鲜事数目
     * @param followees
     * @return
     */
    public int findFeedCount(List<Integer> followees) {
       return  feedMapper.findFeedCount(followees);
    }
}
