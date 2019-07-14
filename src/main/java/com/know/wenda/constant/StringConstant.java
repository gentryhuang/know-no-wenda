package com.know.wenda.constant;

/**
 * StringConstant
 *
 * @author hlb
 */
public class StringConstant {

    /**
     * 阿里云图片储存路径
     */
   public interface OssImageUrl{
         String OSS_IMAGE_URL = "https://zfw-avatar.oss-cn-hangzhou.aliyuncs.com/%d.jpg";
    }


    /**
     * 凭证
     */
   public interface Token{
        /**
         * redis 中token对应的key
         */
        String TOKEN = "token:%s";

        /**
         * db 中的token
         */
        String KEY_TOKNE = "token";
    }

    /**
     * 用户信息
     */
    public interface  UserInfo{
         String USERDO = "userDO";
    }

    /**
     * solr的地址
     *
     **/
   public interface SolrUrl{
       String SOLR_URL = "http://192.168.25.100:8983/solr/know_no_wenda";
    }

    /**
     * 索引关键字
     */
    public interface IndexField{
        /**
         * 问题标题
         */
        String QUESTION_TITLE_FIELD = "question_title";
        /**
         * 问题内容
         */
        String QUESTION_CONTENT_FIELD = "question_content";
        /**
         * 复制域字段
         */
        String QUESTION_COPY_FIELD = "question_key:";
    }

    /**
     * 时间格式
     */
    public interface TimeFormat{
        /**
         * 时间格式化的格式
         */
        String TIME_FORMAT = "yyyyMMddhhmmss";
    }

    /**
     * 网站敏感词过滤
     */
    public interface SensitiveFilterString{
        /**
         * 过滤词的配置文件
         */
        String SENSITIVE_FILTER_FILE_LOCATION = "config/SensitiveWords.txt";

        /**
         * 屏蔽代替
         */
        String SENTITIVE_REPLACE = "**";
    }

    /**
     * 私信请求消息
     */
    public interface MessageString{
        /**
         * 通知过住问题
         */
        String QUESTION_URL = "<a href='http://%s:%d/question/%d'>用户%s关注了你的问题</a>";
        /**
         * 通知有粉丝了
         */
        String USER_URL = "<a href='http://%s:%d/user/%d'>用户%s关注了你</a>";

        /**
         * 感谢通知
         */
        String GRATITUDE_URL =  "<a href='http://%s:%d/question/%s'>用户%s感谢了你的话题</a>";

        /**
         * 用户赞了
         */
        String LIKE_URL = "<a href='http://%s:%d/question/%s'>用户%s赞了你的评论</a>";
    }


}
