package com.example.service;

import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Metadata {

    public String getMatadata(MultipartFile multipartFile , int users_id) throws IOException, TikaException, SAXException;
}
