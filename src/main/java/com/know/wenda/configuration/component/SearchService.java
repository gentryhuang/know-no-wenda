package com.know.wenda.configuration.component;

import com.know.wenda.constant.StringConstant;
import com.know.wenda.domain.QuestionDO;
import com.know.wenda.execption.ConsumerException;
import com.know.wenda.execption.GlobalErrorEnum;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * SearchService
 *
 * @author hlb
 */
@Component
public class SearchService {

  @Resource
  private HttpSolrClient httpSolrClient;

    /**
     *
     * @param keyword 关键词
     * @param offset  翻页
     * @param count   条数
     * @return
     * @throws Exception
     */
    public List<QuestionDO> searchQuestion(String keyword, int offset, int count) {
        try{
            List<QuestionDO> questionDOList = new ArrayList<>();
            // 创建搜索条件query,这里使用复制域前缀加关键词，也可以指定默认搜索域
            SolrQuery query = new SolrQuery(StringConstant.IndexField.QUESTION_COPY_FIELD+keyword);
            // 设置多少行
            query.setRows(count);
            // 偏移量
            query.setStart(offset);
            // 设置高亮信息
            // 1 开启高亮
            query.setHighlight(true);
            //2 指定前后缀
            query.setHighlightSimplePre("<em>");
            query.setHighlightSimplePost("</em>");
            // 3 指定高亮的字段
            query.addHighlightField(StringConstant.IndexField.QUESTION_TITLE_FIELD);
            query.addHighlightField(StringConstant.IndexField.QUESTION_CONTENT_FIELD);
            // 执行查找
            QueryResponse response = httpSolrClient.query(query);
            // 获取高亮信息 (key:doc的id value: 对应的文档信息 )
            Map<String,Map<String,List<String>>> highlight = response.getHighlighting();
            for (Map.Entry<String, Map<String, List<String>>> entry :highlight.entrySet()) {
                QuestionDO questionDO = new QuestionDO();
                questionDO.setId(Integer.parseInt(entry.getKey()));
                // 是否包含content
                if (entry.getValue().containsKey(StringConstant.IndexField.QUESTION_CONTENT_FIELD)) {
                    // 其实就一个，只是结构就这样
                    List<String> contentList = entry.getValue().get(StringConstant.IndexField.QUESTION_CONTENT_FIELD);
                    if (contentList.size() > 0) {
                        questionDO.setContent(contentList.get(0));
                    }
                }
                // 是否包含title
                if (entry.getValue().containsKey(StringConstant.IndexField.QUESTION_TITLE_FIELD)) {
                    // 其实就一个，只是结构就这样
                    List<String> titleList = entry.getValue().get(StringConstant.IndexField.QUESTION_TITLE_FIELD);
                    if (titleList.size() > 0) {
                        questionDO.setTitle(titleList.get(0));
                    }
                }
                questionDOList.add(questionDO);
            }
            return questionDOList;
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.SOLR_HANDLER, KnwLoggerMarkers.SOLR,KnwLoggerFactory.formatLog("solr搜索异常",keyword),e);
            throw new ConsumerException(GlobalErrorEnum.SEARCH_EXCEPTION.getCode(),GlobalErrorEnum.SEARCH_EXCEPTION.getMessage());
        }
    }

    /**
     * 对数据进行索引
     * @param questionId
     * @param title
     * @param content
     * @return
     * @throws Exception
     */
    public boolean indexQuestion(int questionId, String title, String content)  {
        try{
            // 创建一个文档
            // 一个文档中必须包含一个id域，所有要添加的域名称必须已经在配置文件中定义过了
            SolrInputDocument doc =  new SolrInputDocument();
            doc.setField("id", questionId);
            doc.setField(StringConstant.IndexField.QUESTION_TITLE_FIELD, title);
            doc.setField(StringConstant.IndexField.QUESTION_CONTENT_FIELD, content);
            // 将文档添加到索引库，指定在多长时间返回
            UpdateResponse response = httpSolrClient.add(doc, 1000);
            return response != null && response.getStatus() == 0;
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.SOLR_HANDLER, KnwLoggerMarkers.SOLR,KnwLoggerFactory.formatLog("solr数据索引失败",questionId,title,content),e);
            throw new ConsumerException(GlobalErrorEnum.SOLR_INDEX_EXCEPTION.getCode(),GlobalErrorEnum.SOLR_INDEX_EXCEPTION.getMessage());
        }

    }

    /**
     * 删除对应的索引
     * @param questionId
     * @return
     * @throws IOException
     */
    public boolean deleteQuestion(int questionId)  {
        try{
            httpSolrClient.deleteById(String.valueOf(questionId));
            httpSolrClient.commit();
            return Boolean.TRUE;
        }catch (Exception e){
            LoggerUtil.error(KnwLoggerFactory.SOLR_HANDLER,KnwLoggerMarkers.SOLR,KnwLoggerFactory.formatLog("solr数据索引删除失败",questionId),e);
            return Boolean.FALSE;
        }
    }

}
