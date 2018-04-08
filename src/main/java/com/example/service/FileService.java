package com.example.service;

import com.example.model.File;
//import org.apache.solr.client.solrj.SolrServerException;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface FileService {
    public List<File> findAllFiles();
    public List<File> findFileByfile_name(String name);
    public List<File> fildFileByfile_name_metadata(String name);
    public List<File> findFileByuser_id();
    public List<File> findFileByUserIdGiven(int id);
    public int findFileNumberByUserId(int id);
    public int  findFileNumberByuser_idAndtype(String type);
    public List<String> findFilesByType(String type);
    public List<File> findAllFilesByType(String type);
    public List<File> findFileBySpaceFreename(String name);
    public ArrayList<ArrayList<String>> getFilePaths(List<String> fileLocation);
    public void saveFile(MultipartFile file,String extention,String privacy) throws TikaException, SAXException, IOException;
    public void editFile(String originalName,String newName, String author, String keywords,String privacy);
    public void deleteFile(String name);
}
