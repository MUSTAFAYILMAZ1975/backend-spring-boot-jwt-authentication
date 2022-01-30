package com.javabackend.northwind.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javabackend.northwind.business.abstracts.UserService;
import com.javabackend.northwind.core.dataAccess.UserDao;
import com.javabackend.northwind.core.security.models.User;
import com.javabackend.northwind.core.utilities.results.DataResult;
import com.javabackend.northwind.core.utilities.results.Result;
import com.javabackend.northwind.core.utilities.results.SuccessDataResult;
import com.javabackend.northwind.core.utilities.results.SuccessResult;

@Service
public class UserManager implements UserService{

	private UserDao userDao;

	@Autowired
	public UserManager(UserDao userDao) {
		super();
		this.userDao = userDao;
	}

	@Override
	public Result add(User user) {
		this.userDao.save(user);
		return new SuccessResult("Kullanıcı eklendi");
	}

	@Override
	public DataResult<User> findByEmail(String email) {
		return new SuccessDataResult<User>(this.userDao.findByEmail(email)
				,"Kullanıcı bulundu");
	}

	@Override
	public DataResult<List<User>> getAll() {
		
	
		return new SuccessDataResult<List<User>>
		(this.userDao.findAll(),"Data listelendi");	
	}

	/*
	 * @Override public Result register(User user) { this.userDao.save(user); return
	 * new SuccessResult("Kullanıcı eklendi"); }
	 */

	
	
				

	}