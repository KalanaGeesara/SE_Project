package com.example.service;

import org.apache.solr.client.solrj.SolrServerException;
//
import java.io.IOException;

public interface SolrService {

    public void saveToSolr(int id, String meta, String originalname);

    public void deleteFromSolr(int id);
}
