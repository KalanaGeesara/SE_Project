package com.example.repository;

import com.example.model.File;
import org.apache.cassandra.cli.CliParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository("fileRepository")
public interface FileRepository extends JpaRepository<File,Integer> {

//File findByFile_name(String file_name);
//    File findByFile_name(String name);
//    File findAllByFile_id(int d);

//    @Override
//    File findByFile_name(String aLong);
//    @Query("SELECT p FROM  File f WHERE (f.file_name) = lastName")
//    public List<File> findByFile_name(String lastName);

//    File findByMetadata(String s);
    List<File> findByFileName(String a);

    List<File> findByUserId(int a);

    List<File> findByUserIdAndAndType(int a,String b);

    List<File> findBySpaceFreeFileName(String name);
//    List<File> findByFileId(int id);
    @Transactional
    List<File> removeFileByFileName(String name);

}
