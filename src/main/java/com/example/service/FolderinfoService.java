package com.example.service;

import java.util.ArrayList;
import java.util.List;

public interface FolderinfoService {

    public void saveFileToFolder(String folderName, String fileName);
    public ArrayList<ArrayList<String>> getFiles(String folderName);
    public void removeFile(String fileName);
}
