package com.example.service;

import com.example.model.SolrSearch;
import com.example.repository.ProductRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service("solrService")
public class SolrServiceImpl implements SolrService {

    @Autowired
    private ProductRepository productRepository;
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
    @Override
    public void saveToSolr(int id, String meta, String originalname){
        SolrSearch solrSearch = new SolrSearch();
        solrSearch.setId(id);
        solrSearch.setName(meta);
        solrSearch.setOriginalName(originalname);
        productRepository.save(solrSearch);
    }

    @Override
    public void deleteFromSolr(int id){
        productRepository.delete(id);
    }
}
