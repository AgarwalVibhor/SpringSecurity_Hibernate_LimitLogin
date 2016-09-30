package com.tcs.dao;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcs.handlers.MyCustomEncoder;
import com.tcs.model.User;
import com.tcs.model.UserAttempts;

@Repository
public class UserDaoImpl implements UserDaoInterface {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private MyCustomEncoder encoder;
	
	private static final int MAX_ATTEMPTS = 3;

	@Override
	@Transactional(readOnly = false)
	public String insertUser(User user) {
		
		System.out.println("Inside insertUser.");
		
		Session session = sessionFactory.getCurrentSession();
		Transaction t = session.beginTransaction();
		
		if(user != null)
		{
			String password = user.getPassword();
			String hashedPassword = encoder.encode(password);
			user.setPassword(hashedPassword);
		}
		System.out.println("Both Queries for insert get fired simultaneously.");
		String username = (String) session.save(user);
		t.commit();
		
		System.out.println("Insert accountNonLocked : " + user.isAccountNonLocked());
		
		return username;
	}


	@Override
	@Transactional(readOnly = true)
	public User findUserByUsername(String username) {
		
		Session session = sessionFactory.getCurrentSession();
		Transaction t = session.beginTransaction();
		
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.ilike("username", username));
		System.out.println("First Query ::findUserByUsername:: gets fired.");
		User user = (User) cr.uniqueResult();
		System.out.println("Second Query ::findUserByUsername:: gets fired.");
		user.getUserRoles().iterator();
		t.commit();
		return user;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateFailAttempts(String username) {
		
		System.out.println("Inside updateFailAttempts.");
		
		if(isUserExists(username))
		{
			Session session = sessionFactory.getCurrentSession();
			Transaction t = session.beginTransaction();
			
			Criteria cr = session.createCriteria(User.class);
			cr.add(Restrictions.ilike("username", username));
			System.out.println("First Query for User gets fired.");
			User user = (User) cr.uniqueResult();
			System.out.println("Second Query for UserAttempts gets fired.");
			UserAttempts userAttempts = user.getUserAttempts();
			if(userAttempts == null)
			{
				UserAttempts attempts = new UserAttempts();
				attempts.setUser(user);
				attempts.setAttempts(1);
				attempts.setLastModified(new Date());
				System.out.println("Query fired for saving fail attempts for first time.");
				session.save(attempts);
			}
			else
			{
				int attempts = userAttempts.getAttempts();
				if(attempts >= MAX_ATTEMPTS)
				{
					user.setAccountNonLocked(false);
					System.out.println("Query for locking account gets fired.");
					session.update(user);
					
					t.commit();
					
					throw new LockedException("User Account is locked !!");
				}
				else
				{
					userAttempts.setAttempts(attempts + 1);
					userAttempts.setLastModified(new Date());
					System.out.println("Query fired for updating fail attempts.");
					session.update(userAttempts);
				}
			}
			t.commit();
		}
		
		
	}

	@Override
	@Transactional(readOnly = false)
	public void resetFailAttempts(String username) {
		
		System.out.println("Inside resetFailAttempts");
		
		if(isUserExists(username))
		{
			Session session = sessionFactory.getCurrentSession();
			Transaction t = session.beginTransaction();
			
			Criteria cr = session.createCriteria(User.class);
			cr.add(Restrictions.ilike("username", username));
			System.out.println("First Query for retrieving User gets fired.");
			User user = (User) cr.uniqueResult();
			System.out.println("Second Query for retrieving UserAttempts gets fired.");
			UserAttempts userAttempts = user.getUserAttempts();
			
			user.setAccountNonLocked(true);
			System.out.println("Query for updating User gets fired.");
			session.update(user);
			//session.saveOrUpdate(user);
			//session.merge(user);
			
			userAttempts.setAttempts(0);
			userAttempts.setLastModified(null);
			System.out.println("Query for updating UserAttempts gets fired.");
			session.update(userAttempts);
			
			t.commit();
			
		}
		
	}

	@Transactional(readOnly = true)
	private boolean isUserExists(String username)
	{
		Session session = sessionFactory.getCurrentSession();
		Transaction t = session.beginTransaction();
		
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.ilike("username", username));
		User user = (User) cr.uniqueResult();
		t.commit();
		if(user != null)
		{
			return true;
		}
		else
			return false;
	}

	@Override
	@Transactional(readOnly = true)
	public UserAttempts getUserAttempts(String username) {
		
		System.out.println("Inside getUserAttempts");
		
		Session session = sessionFactory.getCurrentSession();
		Transaction t = session.beginTransaction();
		
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.ilike("username", username));
		System.out.println("First Query fired for retrieving User.");
		User user = (User) cr.uniqueResult();
		System.out.println("Second Query fired for retrieving UserAttempts.");
		UserAttempts userAttempts = user.getUserAttempts();
		
		t.commit();
		
		return userAttempts;
	}
	
	

}
