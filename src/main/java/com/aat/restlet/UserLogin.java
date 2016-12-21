package com.aat.restlet;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.aat.datastore.User;
import com.googlecode.objectify.ObjectifyService;

public class UserLogin extends ServerResource{
		private final static String LOGIN = "login";
		private final static String LOGOUT = "logout";
	
	@Post
	   public String passwordCheck(){
		   String userEmail = getAttribute("email");
		   String userInputPassword = getAttribute("password");
		   UserLogin user=new UserLogin();
		   if(userInputPassword.equals(user.retrievePassword(userEmail))){
			   return LOGIN;
		   }
		   else{
			   return LOGOUT;
		   }
	   }
	
	/**
	    * @param String email
	    * @return User user
	    * Retrieves a user by email.
	    * */
	   private User retrieveUser (String email){
		   User user = ObjectifyService.ofy()
				   .load()
				   .type(User.class)
				   .id(Long.parseLong(email)).now();	   	   
		   return user;
	   } 

	/**
	    * @param String email
	    * @return user's password for checking
	    * Retrieves user's password by email.
	    * */
		private String retrievePassword(String email){
		   String userEmail = email;
		   User user = retrieveUser(userEmail);
		   return user.getPassword();
	   }   
	
}
