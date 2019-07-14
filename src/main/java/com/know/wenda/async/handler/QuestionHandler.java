package com.know.wenda.async.handler;

import com.know.wenda.async.EventHandler;
import com.know.wenda.async.EventModel;
import com.know.wenda.configuration.component.SearchService;
import com.know.wenda.constant.EventType;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * AddQuestionHandler
 *
 * @author hlb
 */
@Component
public class QuestionHandler implements EventHandler {
    @Autowired
    private SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        try {
            // TODO 失败了应该有补偿的
            searchService.indexQuestion(model.getEntityId(), model.getExt("title"), model.getExt("content"));
        } catch (Exception e) {
            LoggerUtil.error(KnwLoggerFactory.SOLR_HANDLER, KnwLoggerMarkers.SOLR,
                    KnwLoggerFactory.formatLog("索引问题失败",model.getEntityId(),model.getExt("title"),model.getExt("content"),e));
        }
    }

    /**
     * 只关心发表问题事件
     * @return
     */
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
