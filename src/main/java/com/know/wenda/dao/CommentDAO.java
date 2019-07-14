package com.know.wenda.dao;

import com.know.wenda.dao.mapper.CommentMapper;
import com.know.wenda.domain.CommentDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CommentDAO
 *
 * @author shunhua
 */
@Repository
public class CommentDAO {

    @Resource
    private CommentMapper commentMapper;


    /**
     * 新增评论
     *
     * @param commentDO
     * @return
     */
    public int insert(CommentDO commentDO) {
        int result = commentMapper.insert(commentDO);
        return result;
    }

    /**
     * 根据PK查询
     *
     * @param id
     * @return
     */
    public CommentDO get(int id) {
        CommentDO commentDO = commentMapper.get(id);
        return commentDO;
    }

    /**
     * 根据entity查询列表
     *
     * @param entityId
     * @param entityType
     * @return
     */
    public List<CommentDO> findByEntity(int entityId, int entityType) {
        Map<String, Integer> map = new HashMap<>(2);
        map.put("entityId", entityId);
        map.put("entityType", entityType);
        List<CommentDO> comments = commentMapper.findByEntity(map);
        return comments;
    }

    /**
     * 根据Entity获得数量
     *
     * @param entityId
     * @param entityType
     * @return
     */
    public int findCommentCountByEntity(int entityId, int entityType) {
        Map<String, Integer> map = new HashMap<>(2);
        map.put("entityId", entityId);
        map.put("entityType", entityType);
        int count = commentMapper.findCommentCountByEntity(map);
        return count;
    }

    /**
     * 根据user获得数量
     *
     * @param userId
     * @return
     */
    public int findCommentCountByUser(int userId) {
        int count = commentMapper.findCommentCountByUser(userId);
        return count;
    }

    /**
     * 修改状态
     *
     * @param commentId
     * @return
     */
    public int updateStatus(int commentId) {
        int result = commentMapper.delete(commentId);
        return result;
    }

    /**
     * 根据用户id获取评论列表
     * @param userId
     * @param entityType
     * @param num
     * @return
     */
    public List<CommentDO> findCommentByUserId(Integer userId,Integer entityType,Integer num) {
        return commentMapper.findCommentByUserId(userId,entityType,num);
    }

    /**
     * 根据用户id获取问题的评论数
     * @param userId
     * @param entityQuestion
     * @return
     */
    public List<CommentDO> findQuestionCommentCountByUserId(Integer userId, int entityQuestion) {
        return commentMapper.findQuestionCommentCountByUserId(userId,entityQuestion);
    }
}
