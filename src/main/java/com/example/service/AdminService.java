package com.example.service;

import com.example.model.File;
import com.example.model.User;

import java.util.ArrayList;
import java.util.List;

public interface AdminService {
    public ArrayList<ArrayList<String>> getFilesForAdmin(List<File> fileList);
    public ArrayList<ArrayList<String>> getUserForAdmin(List<User> userList);
}
