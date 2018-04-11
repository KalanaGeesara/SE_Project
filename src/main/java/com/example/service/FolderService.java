package com.example.service;

import java.util.List;

public interface FolderService {

    public void saveFolder(String folderName);

    public List<String> getFolderNames();
}
