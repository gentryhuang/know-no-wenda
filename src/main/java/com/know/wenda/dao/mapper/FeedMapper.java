package com.know.wenda.dao.mapper;

import com.know.wenda.domain.FeedDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


/**
 * FeedMapper
 *
 * @author shunhua
 *
 */
@Mapper
public interface FeedMapper {

    /**
     * 新增
     * @param feedDO
     * @return
     */
    int insert(FeedDO feedDO);

    /**
     * 根据PK查询
     * @param id
     * @return
     */
    FeedDO get(int id);

    /**
     * 多条件查询
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    List<FeedDO> selectUserFeeds(@Param("maxId") int maxId,
                                 @Param("userIds") List<Integer> userIds,
                                 @Param("count") int count);

    /**
     * 获取当前用户关心的新鲜事数目
     * @param userIds
     * @return
     */
    int findFeedCount(@Param("userIds") List<Integer> userIds);
}
