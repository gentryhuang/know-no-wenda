package com.know.wenda.configuration.component;

import com.know.wenda.constant.StringConstant;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 前缀树算法
 *
 * SensitiveService
 *
 * @author hlb
 */
@Component
public class SensitiveFilter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    /**
     * 默认敏感词替换符
     */
    private static final String DEFAULT_REPLACEMENT = StringConstant.SensitiveFilterString.SENTITIVE_REPLACE;

    /**
     * 敏感对应树的节点结构
     */
    private class TrieNode {

        /**
         * 是不是关键词的结尾
         */
        private boolean end = false;

        /**
         * 当前节点下所有的子节点
         *
         * key：子节点对应的字符，value： 对应的子节点
         */
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        /**
         * 给当前节点加子节点
         *
         * 向指定位置添加节点，位置使用字符进行标识
         */
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        /**
         * 获取当前节点的下个节点
         */
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        /**
         * 判断是否是结尾
         * @return
         */
        boolean isKeywordEnd() {
            return end;
        }

        /**
         * 设置节点是否是结尾节点
         * @param end
         */
        void setKeywordEnd(boolean end) {
            this.end = end;
        }

    }

    /**
     * 初始化根节点
     */
    private TrieNode rootNode = new TrieNode();

    /**
     * 构建敏感词前缀树，一个字符串是树的一个分支
     * @param lineTxt
     */
    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;
        // 循环每个字符
        for (int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);
            // 过滤空格
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;

            if (i == lineTxt.length() - 1) {
                // 关键词结束， 设置结束标志
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        // 敏感词被什么过滤，这里使用***
        String replacement = DEFAULT_REPLACEMENT;
        // 收集处理后的字符串
        StringBuilder result = new StringBuilder();
        // 敏感词前缀树的根指针
        TrieNode tempNode = rootNode;
        // 记录遍历位置的指针
        int begin = 0;
        // 用与和前缀树节点值进行比对的指针
        int position = 0;
        while (position < text.length()) {
            char c = text.charAt(position);
            // 空格直接跳过
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            // 取出根节点下面挂的对应值，用于和当前字符比对
            tempNode = tempNode.getSubNode(c);

            // 字符串当前位置匹配结束
            if (tempNode == null) {
                // 以begin开始的字符串不存在敏感词
                result.append(text.charAt(begin));
                // 跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                // 回到树初始节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词， 从begin到position的位置用replacement替换掉，因为position一直在和前缀树比较，在往后移，如果匹配到了敏感词，那么就替换从begin到position这段串
                result.append(replacement);
                // 更新位置
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }
        // 如果position走完了字符串，那么就说明从begin到position这段不是敏感词，就把它加入最终的字符串中
        result.append(text.substring(begin));
        return result.toString();
    }


    /**
     * 判断是否是一个特别的字符，如空格，符号等
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();
        try {
            // 读取配置文件
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(StringConstant.SensitiveFilterString.SENSITIVE_FILTER_FILE_LOCATION);
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                // 每一行就是一个前缀分支
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

}
