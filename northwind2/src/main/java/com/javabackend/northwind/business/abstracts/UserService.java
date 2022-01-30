package com.javabackend.northwind.business.abstracts;

import java.util.List;

import com.javabackend.northwind.core.security.models.User;
import com.javabackend.northwind.core.utilities.results.DataResult;
import com.javabackend.northwind.core.utilities.results.Result;


public interface UserService {
	Result add(User user);
	DataResult<User> findByEmail(String email);
	DataResult<List<User>> getAll();
	//Result register(User user);
	
}
