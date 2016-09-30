package com.tcs.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.tcs.dao.UserDaoInterface;
import com.tcs.model.UserAttempts;

@Component("authenticationProvider")
public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {
	
	@Autowired
	private UserDaoInterface dao;
	
	@Override
	@Autowired
	@Qualifier("customService")
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		
		super.setUserDetailsService(userDetailsService);
	}
	
	@Override
	@Autowired
	@Qualifier("customEncoder")
	public void setPasswordEncoder(Object encoder) {
		
		super.setPasswordEncoder(encoder);
	}
	
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		try
		{
			Authentication auth = super.authenticate(authentication);
			dao.resetFailAttempts(authentication.getName());
			return auth;
		}
		catch(BadCredentialsException e)
		{
			System.out.println("BadCredentialsException is thrown.");
			dao.updateFailAttempts(authentication.getName());
			throw e;
		}
		catch(LockedException e)
		{
			System.out.println("LockedException is thrown.");
			String error = "";
			UserAttempts userAttempts = dao.getUserAttempts(authentication.getName());
			if(userAttempts != null)
			{
				error = "User Account is locked !! <br><br>Username : " + authentication.getName() +
						"<br>Last Attempt : " + userAttempts.getLastModified();
			}
			else
			{
				error = e.getMessage();
			}
			throw new LockedException(error);
		}
		
	}

}
