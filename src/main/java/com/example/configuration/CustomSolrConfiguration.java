package com.example.configuration;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.SolrClientFactory;
import org.springframework.data.solr.server.support.MulticoreSolrClientFactory;

@Configuration
@EnableSolrRepositories(value = "com.example", schemaCreationSupport = true, multicoreSupport = true)
public class CustomSolrConfiguration {

    @Value(value = "${spring.data.solr.host}")
    private String solrHost;

    @Bean
    public SolrClientFactory solrServerFactory() {
        return new MulticoreSolrClientFactory(new HttpSolrClient(solrHost));
//        SolrServer server = new CommonsHttpSolrServer("http://index.websolr.com/solr/0a1b2c3d4e5f");
//        return server;
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        SolrTemplate solrTemplate = new SolrTemplate(solrServerFactory());
        solrTemplate.setSolrCore("mydrive");
        return solrTemplate;
    }

}
