package com.example.repository;

import com.example.model.SolrSearch;
import com.example.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    public void saveSolrTest(){
        SolrSearch solrSearch1 = getSolrObject(1000,"metadataoffiles1","kalana1");
        SolrSearch solrSearch2 = getSolrObject(1001,"metadataoffiles2","kalana2");
        SolrSearch solrSearch3 = getSolrObject(1002,"metadataoffiles3","kalana3");
        SolrSearch solrSearch4 = getSolrObject(1003,"metadataoffiles4","kalana4");

        productRepository.save(solrSearch1);
        productRepository.save(solrSearch2);
        productRepository.save(solrSearch3);
        productRepository.save(solrSearch4);

        System.out.println("4 elements added");

    }

    @Test
    public void editSolrTest(){
        SolrSearch solrSearch1 = getSolrObject(1000,"editmetadataoffiles1","kalana1");

        productRepository.save(solrSearch1);

        assertThat(productRepository.findByName("editmetadataoffiles1").get(0).getName()).isEqualTo(solrSearch1.getName());
        assertThat(productRepository.findByName("editmetadataoffiles1").get(0).getOriginalName()).isEqualTo(solrSearch1.getOriginalName());
    }

    @Test
    public void deleteFromSolrTest(){
        productRepository.delete(1000);
        assertThat(productRepository.findByName("editmetadataoffiles1")).isEmpty();
    }

    @Test
    public void findByNameTest() {
        SolrSearch solrSearch1 = getSolrObject(1000,"metadataoffiles1","kalana1");
//        Assert.assertTrue(
//                productRepository.findByName("metadataoffiles1").get(0).getName().equals("metadataoffiles1")
//        );
        assertThat(productRepository.findByName("metadataoffiles1").get(0).getName()).isEqualTo(solrSearch1.getName());
    }

    private SolrSearch getSolrObject(int id, String name, String originalname){
        SolrSearch solrSearch = new SolrSearch();
        solrSearch.setId(id);
        solrSearch.setName(name);
        solrSearch.setOriginalName(originalname);
        return solrSearch;
    }
}
