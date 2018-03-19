package com.example.service;

import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MetadataImpl implements Metadata {
    @Override
    public String getMatadata(MultipartFile file , int users_id) throws IOException, TikaException, SAXException {
        String metadata1 = "";
        String filedirec = "C:\\Users\\Kalana\\IdeaProjects\\SpringSecurityLoginTutorial-master\\upload-dir\\";
        filedirec = filedirec + users_id + file.getOriginalFilename();
        //Parser method parameters
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        org.apache.tika.metadata.Metadata metadata = new org.apache.tika.metadata.Metadata();
        FileInputStream inputstream = new FileInputStream(new java.io.File(filedirec));
        ParseContext context = new ParseContext();

        parser.parse(inputstream, handler, metadata, context);
//        System.out.println("4444444444444444444");
//        System.out.println("Contents of the document:" + handler.toString());
//        System.out.println("Metadata of the document:");
        String[] metadataNames = metadata.names();
//        System.out.println("35555555555555555555555");
        for(String name : metadataNames) {
//            System.out.println(name + ": " + metadata.get(name));
            metadata1 += (name + " : ");
            metadata1 += metadata.get(name);
            metadata1 +=" , ";

        }
        return metadata1;
    }

}
