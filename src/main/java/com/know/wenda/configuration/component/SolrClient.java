package com.know.wenda.configuration.component;

import com.know.wenda.constant.StringConstant;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SolrClient
 *
 * @author hlb
 */

@Configuration
public class SolrClient {

    @Bean
    public HttpSolrClient httpSolrClient(){
        /**
         * 构建solr的HttpSolrClient，用它操作相关数据
         */
        return new HttpSolrClient.Builder(StringConstant.SolrUrl.SOLR_URL).build();
    }
}