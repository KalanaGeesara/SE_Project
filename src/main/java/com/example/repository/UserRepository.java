package com.example.repository;

import com.example.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.User;

import javax.transaction.Transactional;
import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	 User findByEmail(String email);
	 User findById(int id);

	@Transactional
	List<User> removeUserByEmail(String email);
}
