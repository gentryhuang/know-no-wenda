
package com.know.wenda.dao.mapper;

import com.know.wenda.domain.CommentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * CommentMapper
 *
 * @author shunhua
 */
@Mapper
public interface CommentMapper {

    /**
     * 新增评论
     *
     * @param commentDO
     * @return
     */
    int insert(CommentDO commentDO);

    /**
     * 根据PK查询
     *
     * @param id
     * @return
     */
    CommentDO get(int id);

    /**
     * 根据entity查询列表
     *
     * @param map
     * @return
     */
    List<CommentDO> findByEntity(Map<String, Integer> map);

    /**
     * 根据Entity获得数量
     *
     * @param map
     * @return
     */
    int findCommentCountByEntity(Map<String, Integer> map);

    /**
     * 根据user获得数量
     *
     * @param userId
     * @return
     */
    int findCommentCountByUser(int userId);

    /**
     * 根据commentId软删除
     * @param commentId
     * @return
     */
    int delete(int commentId);

    /**
     * 根据用户id获取评论列表
     * @param userId
     * @param entityType
     * @param num
     * @return
     */
    List<CommentDO> findCommentByUserId(@Param("userId") Integer userId,@Param("entityType") Integer entityType,@Param("num") Integer num);

    /**
     * 根据用户id获取问题评论数
     * @param userId
     * @param entityQuestion
     * @return
     */
    List<CommentDO> findQuestionCommentCountByUserId(@Param("userId") Integer userId, @Param("entityType") int entityQuestion);
}
