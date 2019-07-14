package com.know.wenda.service.impl;

import com.know.wenda.dao.CommentDAO;
import com.know.wenda.domain.CommentDO;
import com.know.wenda.service.ICommentService;
import com.know.wenda.configuration.component.SensitiveFilter;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * CommentServiceImpl
 *
 * @author nowcoder
 *
 */
@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<CommentDO> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.findByEntity(entityId, entityType);
    }

    @Override
    public int getCommentCountByEntity(int entityId, int entityType) {
        int count = commentDAO.findCommentCountByEntity(entityId, entityType);
        return count;
    }

    @Override
    public int addComment(CommentDO commentDO) {
        try{
            // 过滤html
            commentDO.setContent(HtmlUtils.htmlEscape(commentDO.getContent()));
            // 敏感词过滤
            commentDO.setContent(sensitiveFilter.filter(commentDO.getContent()));
            return commentDAO.insert(commentDO) > 0 ? commentDO.getId() : 0;
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.BUSINESS_LOGGER, KnwLoggerMarkers.BUSINESS_INFO,"数据库操作异常",commentDO);
            return 0;
        }
    }

    @Override
    public int getUserCommentCount(int userId) {
        return commentDAO.findCommentCountByUser(userId);
    }

    @Override
    public boolean deleteComment(int commentId) {
        return commentDAO.updateStatus(commentId) > 0;
    }


    @Override
    public CommentDO getCommentById(int id) {
        return commentDAO.get(id);
    }

    @Override
    public List<CommentDO> getCommentByUserId(Integer userId,Integer entityType,Integer num) {
        return commentDAO.findCommentByUserId(userId,entityType,num);
    }

    @Override
    public List<CommentDO> getQuestionCommentCountByUserId(Integer userId, int entityQuestion) {
        return commentDAO.findQuestionCommentCountByUserId(userId,entityQuestion);
    }
}
