package com.tcs.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tcs.business.UserBusinessInterface;
import com.tcs.model.User;
import com.tcs.model.UserRole;

@Controller
public class LoginController {
	
	@Autowired
	private UserBusinessInterface business;
	
	@RequestMapping(value = "/")
	public ModelAndView start(HttpServletRequest request, HttpServletResponse response)
	{
		return new ModelAndView("redirect:/hello");
	}
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ModelAndView hello(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView model = new ModelAndView("hello");
		model.addObject("title", "Spring Security - Limit Login With Hibernate");
		model.addObject("message", "This page is for both the Users and the Admin guys !!");
		return model;
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView admin(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView model = new ModelAndView("admin");
		model.addObject("title", "Spring Security - Limit Login With Hibernate");
		model.addObject("message", "This page is only for the Admin guys !!");
		return model;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout)
	{
		ModelAndView model = new ModelAndView("login");
		if(error != null)
		{
			model.addObject("message", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}
		if(logout != null)
		{
			model.addObject("logout", "You have successfully logged out !!");
		}
		return model;
	}
	
	public String getErrorMessage(HttpServletRequest request, String key)
	{
		String error = "";
		HttpSession session = request.getSession();
		Exception exception = (Exception) session.getAttribute(key);
		if(exception instanceof BadCredentialsException)
		{
			error = "Invalid Username and Password !!";
		}
		else if(exception instanceof LockedException)
		{
			error = exception.getMessage();
		}
		else
		{
			error = "Invalid Username and Password !!";
		}
		return error;
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView error(HttpServletRequest request, HttpServletResponse response)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user = (UserDetails) authentication.getPrincipal();
		String username  =user.getUsername();
		ModelAndView model = new ModelAndView("403");
		model.addObject("username", username);
		return model;
	}
	
	@RequestMapping(value = "/registerGet", method = RequestMethod.GET)
	public ModelAndView registerGet(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView model = new ModelAndView("register");
		User user = new User();
		model.addObject("userForm", user);
		return model;
	}
	
	@RequestMapping(value = "/registerPost", method = RequestMethod.POST)
	public ModelAndView registerPost(HttpServletRequest request, HttpServletResponse response,
			@Valid @ModelAttribute("userForm") User user, BindingResult result,
			@RequestParam(value = "clerkRole", required = false) String clerkRole,
			@RequestParam(value = "adminRole", required = false) String adminRole,
			@RequestParam(value = "managerRole", required = false) String managerRole)
	{
		
		Set<UserRole> userRoles = new HashSet<UserRole>();
		ModelAndView model = null;
		if(result.hasErrors())
		{ 
			model = new ModelAndView("register");
			return model;
		}
		else if(clerkRole == null)
		{
			model = new ModelAndView("register");
			model.addObject("userForm", user);
			model.addObject("error_message", "You need to select at least one option !!");
			return model;
		}
		else if(clerkRole.equals("No"))
		{
			if(adminRole == null && managerRole == null)
			{
				model = new ModelAndView("register");
				model.addObject("userForm", user);
				model.addObject("message", "You need to select at least 1 role !!");
				return model;
			}
			else if(adminRole == null && managerRole.equals("No"))
			{
				model = new ModelAndView("register");
				model.addObject("userForm", user);
				model.addObject("message", "At least 1 role should be selected !!");
				return model;
			}
			else if(adminRole == null && managerRole.equals("Yes"))
			{
				model = new ModelAndView("register");
				model.addObject("userForm", user);
				model.addObject("message", "Admin Role cannot be left blank. You need to select at least 1 option !!");
				return model;
			}
			else if(adminRole.equals("No") && managerRole == null)
			{
				model = new ModelAndView("register");
				model.addObject("userForm", user);
				model.addObject("message", "You need to have at least 1 role !!");
				return model;
			}
			else if(adminRole.equals("Yes") && managerRole == null)
			{
				model = new ModelAndView("register");
				model.addObject("userForm", user);
				model.addObject("message", "Manager Role cannot be left blank. You need to select at least 1 option !!");
				return model;
			}
			else if(adminRole.equals("No") && managerRole.equals("No"))
			{
				model = new ModelAndView("register");
				model.addObject("userForm", user);
				model.addObject("message", "You need to have at least 1 role !!");
				return model;
			}
			else if(adminRole.equals("No") && managerRole.equals("Yes"))
			{
				UserRole userRole1 = new UserRole();
				userRole1.setRole("ROLE_MANAGER");
				userRole1.setUser(user);
				userRoles.add(userRole1);
				user.setUserRoles(userRoles);
			}
			else if(adminRole.equals("Yes") && managerRole.equals("No"))
			{
				UserRole userRole1 = new UserRole();
				userRole1.setRole("ROLE_ADMIN");
				userRole1.setUser(user);
				userRoles.add(userRole1);
				user.setUserRoles(userRoles);
			}
			else if(adminRole.equals("Yes") && managerRole.equals("Yes"))
			{
				UserRole userRole1 = new UserRole();
				userRole1.setRole("ROLE_ADMIN");
				userRole1.setUser(user);
				userRoles.add(userRole1);
				UserRole userRole2 = new UserRole();
				userRole2.setRole("ROLE_MANAGER");
				userRole2.setUser(user);
				userRoles.add(userRole2);
				user.setUserRoles(userRoles);
			}
		}
		else if(clerkRole.equals("Yes"))
		{
			UserRole userRole1 = new UserRole();
			userRole1.setRole("ROLE_CLERK");
			userRole1.setUser(user);
			userRoles.add(userRole1);
			user.setUserRoles(userRoles);
		}

		String username = business.insertUser(user);
		if(username != null)
		{
			model = new ModelAndView("registerSuccess");
			model.addObject("title", "Spring Security - Limit Login");
			model.addObject("username", username);
		}
		else
		{
			model = new ModelAndView("register");
			model.addObject("userForm", user);
			model.addObject("message", "Sorry! The user could not be registered successfully. Please try again !!");
		}
		return model;
	}

}
