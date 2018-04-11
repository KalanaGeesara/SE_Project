package com.example.service;

import com.example.model.File;
import com.example.model.Folderinfo;
import com.example.model.User;
import com.example.repository.FolderinfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FolderinfoServiceImpl implements FolderinfoService {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FolderinfoRepository folderinfoRepository;

    @Override
    public void saveFileToFolder(String folderName,String fileName){
        User user = userService.getCurrentUser();
        List<File> files = fileService.fildFileByfile_name_metadata(fileName);
        Folderinfo folderinfo = new Folderinfo();
        folderinfo.setFileId(files.get(0).getFile_id());
        folderinfo.setUserId(files.get(0).getUserId());
        folderinfo.setFileName(fileName);
        folderinfo.setFolderName(folderName);
        folderinfoRepository.save(folderinfo);
    }

    @Override
    public ArrayList<ArrayList<String>> getFiles(String folderName){
        User user = userService.getCurrentUser();
        ArrayList<ArrayList<String>> aObject;
// Create the 2D array list
        aObject = new ArrayList<ArrayList<String>>();
        List<Folderinfo> folderinfos = folderinfoRepository.findAllByUserIdAndAndFolderName(user.getId(),folderName);
        for(int i=0;i<folderinfos.size();i++){
            aObject.add(new ArrayList<String>());
        }
        for (int i=0;i<folderinfos.size();i++){
            String fileName = folderinfos.get(i).getFileName();
            String edit = "/edit/"+fileName+"/"+folderName;
            String info = "/info/"+fileName;
            String remove = "/remove/"+fileName+"/"+folderName;
            String delete = "/delete/"+fileName;
            aObject.get(i).add(fileName);
            aObject.get(i).add(edit);
            aObject.get(i).add(info);
            aObject.get(i).add(remove);
            aObject.get(i).add(delete);
        }
        return aObject;
    }

    @Override
    public void removeFile(String fileName){
        List<Folderinfo> result = folderinfoRepository.removeByFileName(fileName);
    }
}
