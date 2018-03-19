package com.example.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

//@SolrDocument(solrCoreName = "test")
public class SolrSearch {

    @Id
    @Field
    private int id;

    @Field("name")
    private String name;

    @Field("originalname")
    private String originalname;

//    @Field("cat")
//    private List<String> category;
//
//    @Field("store")
//    private Point location;

//    public Product() {
//    }
//
//    public Product(int id, String name) {
//        this.id = id;
//        this.name = name;
//    }

    public Integer getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return this.originalname;
    }

    public void setOriginalName(String originalName) {
        this.originalname = originalName;
    }

}
