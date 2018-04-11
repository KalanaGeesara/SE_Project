package com.example.repository;

import com.example.model.Folderinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FolderinfoRepository extends JpaRepository<Folderinfo,Integer> {

    List<Folderinfo> findAllByUserIdAndAndFolderName(int id,String name);

    @Transactional
    List<Folderinfo> removeByFileName(String name);
}
