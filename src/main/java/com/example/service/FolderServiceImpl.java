package com.example.service;

import com.example.model.Folder;
import com.example.model.User;
import com.example.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService{

    @Autowired
    private UserService userService;

    @Autowired
    private FolderRepository folderRepository;
    @Override
    public void saveFolder(String folderName){
        User user = userService.getCurrentUser();
        Folder folder = new Folder();
        folder.setUserId(user.getId());
        folder.setFolderName(folderName);
        folderRepository.save(folder);
    }

    @Override
    public List<String> getFolderNames(){
        User user = userService.getCurrentUser();
        List<String> folderNames = new ArrayList<>();
        List<Folder> folders = folderRepository.findByUserId(user.getId());
        for (int i=0; i< folders.size(); i++){
            folderNames.add(folders.get(i).getFolderName());
        }
        return folderNames;
    }
}
