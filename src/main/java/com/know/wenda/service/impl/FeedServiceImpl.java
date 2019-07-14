package com.know.wenda.service.impl;

import com.know.wenda.dao.FeedDAO;
import com.know.wenda.domain.FeedDO;
import com.know.wenda.service.IFeedService;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * FeedServiceImpl
 *
 * @author nowcoder
 */
@Service
public class FeedServiceImpl implements IFeedService {

    @Autowired
    private FeedDAO feedDAO;

    /**
     * 拉取所关联的好友
     *
     * @param maxId feed表中的id 要小于maxId
     * @param userIds
     * @param count 预期的数量
     * @return
     */
    @Override
    public List<FeedDO> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    /**
     * 当触发事件，增加feedDO
     *
     * @param feedDO
     * @return
     */
    @Override
    public boolean addFeed(FeedDO feedDO) {
        int result = feedDAO.insert(feedDO);
        return result > 0;
    }

    /**
     * 推模式
     *
     * @param id
     * @return
     */
    @Override
    public FeedDO getById(int id) {
        return feedDAO.get(id);
    }

    @Override
    public int findFeedCount(List<Integer> followees) {
        if(CollectionUtils.isEmpty(followees)){
            LoggerUtil.warn(KnwLoggerFactory.BUSINESS_LOGGER, KnwLoggerMarkers.BUSINESS_INFO, KnwLoggerFactory.formatLog("followees为空",followees));
            return 0;
        }
        return feedDAO.findFeedCount(followees);
    }
}
