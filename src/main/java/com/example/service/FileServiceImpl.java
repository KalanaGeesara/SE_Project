package com.example.service;

import com.example.model.File;
//import com.example.model.Product;
import com.example.model.SolrSearch;
import com.example.model.User;
import com.example.repository.FileRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
//import org.apache.solr.client.solrj.SolrServerException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.List;

@Service("fileServices")
public class FileServiceImpl implements FileService {

    @Autowired
    private UserRepository userRepository;

   @Autowired
   private FileRepository fileRepository;

   @Autowired
   private ProductRepository productRepository;

//    private SolrService solrService;
    @Override
    public List<File> findFileByfile_name(String file_name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        String file_name2 = ( user.getId()) + file_name.toString();
        System.out.println(file_name.toString());
        System.out.println(file_name2);
        return (fileRepository.findByFileName(file_name2));
//        return (null);
    }

    @Override
    public List<File> fildFileByfile_name_metadata(String name) {
        return (fileRepository.findByFileName(name));
    }

    @Override
    public List<File> findFileByuser_id(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        int users_id = user.getId();
        return (fileRepository.findByUserId(users_id));
//        return null;
    }

    @Override
    public List<File> findFileByuser_idAndtype(String type){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        int users_id = user.getId();
        return (fileRepository.findByUserIdAndAndType(users_id,type));
    }
    @Override
    public void saveFile(MultipartFile file,String extention) throws TikaException, SAXException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        File newFile = new File();
        MetadataImpl metadataImpl = new MetadataImpl();
        newFile.setUserId(user.getId());
//        System.out.println(file.getName());
//        System.out.println(file.getOriginalFilename());
        String fileUploadName = user.getId() + file.getOriginalFilename();
        newFile.setFileName(fileUploadName);
        newFile.setType(extention);
        newFile.setFile_path("http://localhost:8080/files/"+file.getOriginalFilename());
        String meta = metadataImpl.getMatadata(file, user.getId());
        System.out.println(metadataImpl.getMatadata(file, user.getId()));
        newFile.setMetadata(meta);
        fileRepository.save(newFile);
        List<File> file2 = fileRepository.findByFileName(fileUploadName);
        System.out.println(fileUploadName);
        int file_id = file2.get(0).getFile_id();
//        SolrServiceImpl solrServiceimpl = new SolrServiceImpl();

        String fileName = file2.get(0).getName();
        String fileAuthor = file2.get(0).getAuthor();
        meta += fileName+" "+fileAuthor+" ";
        System.out.println(file2.get(0).getFile_id());
        SolrSearch newProduct = new SolrSearch();
        newProduct.setId(file_id);
        newProduct.setName(meta);
        newProduct.setOriginalName(fileUploadName);
        productRepository.save(newProduct);
        System.out.println("dddddddddddddddddd");
//        solrService.saveToSolr(meta, file_id);

    }
    @Override
    public void editFile(String OriginalName,String newName, String author, String keywords){
        List<File> originalFile = fileRepository.findByFileName(OriginalName);
        File file = new File();
        file.setFile_id(originalFile.get(0).getFile_id());
        file.setFile_path(originalFile.get(0).getFile_path());
        file.setMetadata(originalFile.get(0).getMetadata());
        file.setUserId(originalFile.get(0).getUserId());
        file.setFileName(originalFile.get(0).getFileName());
        file.setType(originalFile.get(0).getType());
        file.setName(newName);
        file.setAuthor(author);
        file.setKeywords(keywords);
        fileRepository.save(file);
        String meta = originalFile.get(0).getMetadata();
        meta+= newName+" "+author+" "+keywords+" ";
        SolrSearch newProduct = new SolrSearch();
        newProduct.setId(originalFile.get(0).getFile_id());
        newProduct.setName(meta);
        newProduct.setOriginalName(originalFile.get(0).getFileName());
        productRepository.save(newProduct);
    }

}
