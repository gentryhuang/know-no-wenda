package com.know.wenda.service;

import com.know.wenda.domain.FeedDO;

import java.util.List;

/**
 * IFeedService
 *
 * @author shunhua
 */
public interface IFeedService {

    /**
     * 多条件查询
     *
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    List<FeedDO> getUserFeeds(int maxId, List<Integer> userIds, int count);

    /**
     * 新增Feed
     *
     * @param feedDO
     * @return
     */
    boolean addFeed(FeedDO feedDO);

    /**
     * 根据PK查询
     *
     * @param id
     * @return
     */
    FeedDO getById(int id);

    /**
     * 查询当前用户关心的新鲜事的条数
     * @param followees
     * @return
     */
    int findFeedCount(List<Integer> followees);
}
