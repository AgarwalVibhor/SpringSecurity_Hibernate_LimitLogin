package com.tcs.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcs.dao.UserDaoInterface;
import com.tcs.model.User;

@Service("userService")
public class UserBusinessImpl implements UserBusinessInterface {
	
	@Autowired
	private UserDaoInterface dao;

	@Override
	@Transactional(readOnly = false)
	public String insertUser(User user) {
		
		return dao.insertUser(user);
	}


}
