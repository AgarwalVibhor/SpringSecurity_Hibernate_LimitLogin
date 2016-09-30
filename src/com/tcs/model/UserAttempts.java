package com.tcs.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


@Entity
@Table(name = "user_attempts_limit", catalog = "test")
public class UserAttempts {
	
	private int id;
	private User user;
	private int attempts;
	private Date lastModified;
	
	public UserAttempts() {
		super();
		this.attempts = 0;
	}

	public UserAttempts(int id, User user, int attempts, Date lastModified) {
		super();
		this.id = id;
		this.user = user;
		this.attempts = attempts;  // These attempts correspond to the fail attempts
		this.lastModified = lastModified;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "username_attempt_id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "username", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "attempts", nullable = false)
	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	
	@Column(name = "last_modified", nullable = true)
	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	

	
	
	
	

}
