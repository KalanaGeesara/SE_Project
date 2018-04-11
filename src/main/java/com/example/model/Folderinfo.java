package com.example.model;

import javax.persistence.*;

@Entity
@Table(name = "folderinfo")
public class Folderinfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "folderinfoId")
    private int folderinfoId;

    @Column(name = "fileId")
    private int fileId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "folderName")
    private String folderName;

    @Column(name = "fileName")
    private String fileName;

    public int getFolderinfoId() {
        return folderinfoId;
    }

    public void setFolderinfoId(int folderinfoId) {
        this.folderinfoId = folderinfoId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
