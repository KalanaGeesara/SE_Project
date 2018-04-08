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
import java.util.ArrayList;
import java.util.List;

@Service("fileServices")
public class FileServiceImpl implements FileService {

//    @Autowired
//    private UserRepository userRepository;

   @Autowired
   private FileRepository fileRepository;

   @Autowired
   private ProductRepository productRepository;

   @Autowired
   private UserService userService;

   @Autowired
   private SolrService solrService;


//    private SolrService solrService;

    @Override
    public List<File> findAllFiles(){
        return(fileRepository.findAll());
    }
    @Override
    public List<File> findFileByfile_name(String file_name) {
        User user = userService.getCurrentUser();
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
        User user = userService.getCurrentUser();
        int users_id = user.getId();
        return (fileRepository.findByUserId(users_id));
//        return null;
    }

    @Override
    public List<File> findFileByUserIdGiven(int id){
        return (fileRepository.findByUserId(id));
    }
    @Override
    public int findFileNumberByUserId(int id){
        List<File> files = fileRepository.findByUserId(id);
        return files.size();
    }

    @Override
    public int findFileNumberByuser_idAndtype(String type){
        User user = userService.getCurrentUser();
        int users_id = user.getId();
        List<File> file = (fileRepository.findByUserIdAndAndType(users_id,type));
        return file.size();
    }
    @Override
    public List<String> findFilesByType(String type){
        List<String> fileNames = new ArrayList<String>();
        for(File i:findFileByuser_id()){                    //get all the files uploaded by logged in user

            // add only image files to a list
            if(i.getType().equals(type)){
                fileNames.add(i.getFileName());
            }
        }
        return fileNames;
    }

    @Override
    public List<File> findAllFilesByType(String type){
        return fileRepository.findByType(type);
    }

    @Override
    public List<File> findFileBySpaceFreename(String name){
        List<File> files = fileRepository.findBySpaceFreeFileName(name);
        return files;
    }
    @Override
    public ArrayList<ArrayList<String>> getFilePaths(List<String> fileLocation){
        ArrayList<ArrayList<String>> aObject;
// Create the 2D array list
        aObject = new ArrayList<ArrayList<String>>();

// Add an element to the first dimension

        for(int i=0;i<fileLocation.size();i++){
            aObject.add(new ArrayList<String>());
        }
        for(int j=0;j<fileLocation.size();j++){
//            aObject.get(j).add(new String("Quarks"));
            String s = fileLocation.get(j);
            String [] part = s.split("/");
            List<File> files = findFileBySpaceFreename(part[1].replaceAll("\\s+",""));
            System.out.println(files);
            String filePart = "files/"+part[1];
            String editPart = "edit/"+part[1];
            String infoPart = "info/"+part[1].replaceAll("\\s+","");
            String deletePart = "delete/"+part[1];
            aObject.get(j).add(filePart);
            aObject.get(j).add(editPart);
            aObject.get(j).add(infoPart);
            aObject.get(j).add(deletePart);
        }
        return aObject;
    }
    @Override
    public void saveFile(MultipartFile file,String extention, String privacy) throws TikaException, SAXException, IOException {
        User user = userService.getCurrentUser();
        System.out.println("llfl,,l,lddddddddddddddddddddddddddddd");
        System.out.println(file.getSize());
        System.out.println(file.getBytes());
        File newFile = new File();
        MetadataImpl metadataImpl = new MetadataImpl();
        newFile.setUserId(user.getId());
//        System.out.println(file.getName());
//        System.out.println(file.getOriginalFilename());
        String fileUploadName = user.getId() + file.getOriginalFilename();
        String spaceFreeName = fileUploadName.replaceAll("\\s+","");
        newFile.setFileName(fileUploadName);
        newFile.setSpaceFreeFileName(spaceFreeName);
        newFile.setType(extention);
        newFile.setPrivacy(privacy);
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
        solrService.saveToSolr(file_id,meta,fileUploadName);
        System.out.println("dddddddddddddddddd");
//        solrService.saveToSolr(meta, file_id);

    }
    @Override
    public void editFile(String OriginalName,String newName, String author, String keywords,String privacy){
        List<File> originalFile = fileRepository.findByFileName(OriginalName);
        File file = new File();
        file.setFile_id(originalFile.get(0).getFile_id());
        file.setFile_path(originalFile.get(0).getFile_path());
        file.setMetadata(originalFile.get(0).getMetadata());
        file.setUserId(originalFile.get(0).getUserId());
        file.setFileName(originalFile.get(0).getFileName());
        file.setType(originalFile.get(0).getType());
        file.setSpaceFreeFileName(originalFile.get(0).getSpaceFreeFileName());
        file.setName(newName);
        file.setAuthor(author);
        file.setKeywords(keywords);
        file.setPrivacy(privacy);
        fileRepository.save(file);
        String meta = originalFile.get(0).getMetadata();
        meta+= newName+" "+author+" "+keywords+" ";
        solrService.saveToSolr(originalFile.get(0).getFile_id(),meta,originalFile.get(0).getFileName());
    }

    @Override
    public void deleteFile(String name){
        List<File> result = fileRepository.removeFileByFileName(name);
    }
}
