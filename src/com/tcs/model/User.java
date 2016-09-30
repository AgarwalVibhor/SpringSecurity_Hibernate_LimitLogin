package com.tcs.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "users_limit", catalog = "test")
public class User {

	
	private String fname;
	private String lname;
	private String username;
	private String password;
	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean enabled;
	
	private Set<UserRole> userRoles = new HashSet<UserRole>();
	private UserAttempts userAttempts;
	
	public User() {
		super();
		this.enabled = true;
		this.accountNonLocked = true;
		this.accountNonExpired = true;
		this.credentialsNonExpired = true;
	}

	public User(String fname, String lname, String username, String password,
			boolean accountNonLocked, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean enabled,
			Set<UserRole> userRoles, UserAttempts userAttempts) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.username = username;
		this.password = password;
		this.accountNonLocked = accountNonLocked;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.userRoles = userRoles;
		this.userAttempts = userAttempts;
	}
	
	@Column(name = "first_name", nullable = false)
	@NotEmpty(message = "Please enter your first name !")
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@Column(name = "last_name", nullable = false)
	@NotEmpty(message = "Please enter your last name !")
	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}
	
	@Id
	@Column(name = "username", unique = true, nullable = false, length = 64)
	@NotEmpty(message = "Please enter the username !")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "password", nullable = false, length = 64)
	@NotEmpty(message = "Please enter the password !")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "is_account_non_locked", nullable = false, columnDefinition = "tinyint")
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	
	@Column(name = "is_account_non_expired", nullable = false, columnDefinition = "tinyint")
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	
	@Column(name = "is_credentials_non_expired", nullable = false, columnDefinition = "tinyint")
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	
	@Column(name = "is_enabled", nullable = false, columnDefinition = "tinyint")
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	public UserAttempts getUserAttempts() {
		return userAttempts;
	}

	public void setUserAttempts(UserAttempts userAttempts) {
		this.userAttempts = userAttempts;
	}

	@Override
	public String toString() {
		return "User [fname=" + fname + ", lname=" + lname + ", username="
				+ username + ", password=" + password + ", accountNonLocked="
				+ accountNonLocked + ", accountNonExpired=" + accountNonExpired
				+ ", credentialsNonExpired=" + credentialsNonExpired
				+ ", enabled=" + enabled + ", userRoles=" + userRoles + "]";
	}
	
/*	public void addUserRole(UserRole userRole)
	{
		userRoles.add(userRole);
		userRole.setUser(this);
	}
	
	public void removeUserRole(UserRole userRole)
	{
		userRole.setUser(null);
		this.userRoles.remove(userRole);
	}
	
	public void addUserAttempts(UserAttempts userAttempts)
	{
		this.userAttempts = userAttempts;
		userAttempts.setUser(this);
	}
	
	public void removeUserAttempts()
	{
		if(userAttempts != null)
		{
			userAttempts.setUser(null);
		}
		this.userAttempts = null;
	}     */
	
	

}
