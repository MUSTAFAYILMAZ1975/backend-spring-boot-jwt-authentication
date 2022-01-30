package com.javabackend.northwind.core.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javabackend.northwind.core.security.models.User;



public interface UserDao extends JpaRepository<User, Integer> {
	User findByEmail(String email);

	//User register(User user);
	
}