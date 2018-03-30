package com.example.service;

import com.example.model.File;
//import org.apache.solr.client.solrj.SolrServerException;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

public interface FileService {
    public List<File> findFileByfile_name(String name);
    public List<File> fildFileByfile_name_metadata(String name);
    public List<File> findFileByuser_id();
    public List<File> findFileByuser_idAndtype(String type);
    public void saveFile(MultipartFile file,String extention) throws TikaException, SAXException, IOException;
    public void editFile(String originalName,String newName, String author, String keywords);
    public void deleteFile(String name);
}
