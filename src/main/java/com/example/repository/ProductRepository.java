package com.example.repository;

import java.util.List;

//import com.example.model.Product;
import com.example.model.SolrSearch;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface ProductRepository extends SolrCrudRepository<SolrSearch, Integer> {

    List<SolrSearch> findByNameStartingWith(String name);

    List<SolrSearch> findByName(String d);

//    List<Product> findById(int id);

}
