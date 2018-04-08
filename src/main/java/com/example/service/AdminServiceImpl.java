package com.example.service;

import com.example.model.File;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("adminServices")
public class AdminServiceImpl implements AdminService {

    @Autowired
    private FileService fileService;

    @Override
    public ArrayList<ArrayList<String>> getFilesForAdmin(List<File> fileList) {
        ArrayList<ArrayList<String>> aObject;
        aObject = new ArrayList<ArrayList<String>>();
//        List<File> fileList = fileService.findFileByUserIdGiven(Integer.parseInt(dd));
        for (int i = 0; i < fileList.size(); i++) {
            aObject.add(new ArrayList<String>());
        }
        for (int j = 0; j < fileList.size(); j++) {
//            aObject.get(j).add(new String("Quarks"));
            int fileId = fileList.get(j).getFile_id();
            int userId = fileList.get(j).getUserId();
            String fileOriginalName = fileList.get(j).getFileName();
            String fileName = fileList.get(j).getName();
            String fileAuthor = fileList.get(j).getAuthor();
            String fileType = fileList.get(j).getType();

            aObject.get(j).add("" + fileId);
            aObject.get(j).add("" + userId);
            aObject.get(j).add(fileOriginalName);
            aObject.get(j).add(fileName);
            aObject.get(j).add(fileAuthor);
            aObject.get(j).add(fileType);
            aObject.get(j).add("file/delete/" + fileOriginalName);
        }
        return aObject;
    }

    @Override
    public ArrayList<ArrayList<String>> getUserForAdmin(List<User> userList){
        ArrayList<ArrayList<String>> aObject;
        aObject = new ArrayList<ArrayList<String>>();
//        List<File> fileList = fileService.findFileByUserIdGiven(Integer.parseInt(dd));
        for (int i = 0; i < userList.size(); i++) {
            aObject.add(new ArrayList<String>());
        }
        for (int j = 0; j < userList.size(); j++) {
//            aObject.get(j).add(new String("Quarks"));
            int userId = userList.get(j).getId();
            String firstName = userList.get(j).getName();
            String lastName = userList.get(j).getLastName();
            String email = userList.get(j).getEmail();
            int fileNumber = fileService.findFileNumberByUserId(userId);

            aObject.get(j).add(""+userId);
            aObject.get(j).add(firstName);
            aObject.get(j).add(lastName);
            aObject.get(j).add(email);
            aObject.get(j).add(""+fileNumber);
            aObject.get(j).add("user/delete/"+email);
        }
        return aObject;
    }
}
