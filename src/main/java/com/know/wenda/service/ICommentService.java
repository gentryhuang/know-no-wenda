package com.know.wenda.service;

import com.know.wenda.domain.CommentDO;
import java.util.List;
/**
 * ICommentService
 *
 * @author shunhua
 */
public interface ICommentService {

    /**
     * 新增评论
     *
     * @param commentDO
     * @return
     */
     int addComment(CommentDO commentDO);

    /**
     * 根据PK查询
     *
     * @param id
     * @return
     */
   CommentDO getCommentById(int id);


    /**
     * 根据entity查询列表
     *
     * @param entityId
     * @param entityType
     * @return
     */
    List<CommentDO> getCommentsByEntity(int entityId, int entityType);

    /**
     * 根据Entity获得数量
     *
     * @param entityId
     * @param entityType
     * @return
     */
     int getCommentCountByEntity(int entityId,int entityType);

    /**
     * 根据user获得数量
     *
     * @param userId
     * @return
     */
    int getUserCommentCount(int userId);

    /**
     * 修改状态
     *
     * @param commentId
     * @return
     */
    boolean deleteComment(int commentId);

    /**
     * 获取用户对应的评论列表
     * @param userId
     * @return
     */
    List<CommentDO> getCommentByUserId(Integer userId,Integer entityType,Integer num);

    /**
     * 根据用户id获取问题评论的数量
     * @param userId
     * @param entityQuestion
     * @return
     */
    List<CommentDO> getQuestionCommentCountByUserId(Integer userId, int entityQuestion);
}
