package com.maybank.maybank.app.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maybank.maybank.app.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByUserName(String userName);

@Query("select u from User u where u.userName= :uName")
public User findUserByName (@Param("uName") String uName );
	
}


