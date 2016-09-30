package com.tcs.dao;

import com.tcs.model.User;
import com.tcs.model.UserAttempts;

public interface UserDaoInterface {
	
	public String insertUser(User user);
	
	public User findUserByUsername(String username);
	
	public void updateFailAttempts(String username);
	
	public void resetFailAttempts(String username);
	
	public UserAttempts getUserAttempts(String username);


}
