package com.example.repository;

import com.example.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder,Integer> {

    List<Folder> findByUserId(int id);

}
