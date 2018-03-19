//package com.example.service;
//
//import org.apache.solr.client.solrj.SolrClient;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.impl.HttpSolrClient;
//import org.apache.solr.common.SolrInputDocument;
//
//import java.io.IOException;
//
//public class SolrServiceImpl implements SolrService {
//
//    @Override
//    public void saveToSolr(String meta, int id) throws IOException, SolrServerException {
//
//        String serverURL = "http://localhost:8983/solr/my_tube";
//        SolrClient solr = new HttpSolrClient.Builder(serverURL).build();
//        SolrInputDocument document = new SolrInputDocument();
//        document.addField("id", id);
//        document.addField("metadata", meta);
//        //document.addField("category", "Books");
//        solr.add(document);
//        solr.commit();
//    }
//}
