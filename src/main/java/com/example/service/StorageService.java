package com.example.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file , int users_id);

    Stream<Path> loadAll();

    Stream<Path> loadAll2(List<String> s);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    void deleteFile(String filename) throws IOException;

}
