package com.aat.restlet;

import org.restlet.data.Form;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.aat.datastore.User;
import com.googlecode.objectify.ObjectifyService;

public class UserLogin extends ServerResource{
	
	private final static String LOGIN = "login";
	private final static String LOGOUT = "logout";
	
	@Get
	   public String retrieve(){
		   String userEmail = getAttribute("email");
		   User user = retrieveUser(userEmail);
		   return user.getPassword();
	   }
	
	@Post
	   public String passwordCheck(){
		   //String userEmail = getAttribute("email");
		   String userPassword = getAttribute("password");
		   UserLogin user=new UserLogin();
		   if(userPassword.equals(user.retrieve())){
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
	   
	   
	   
}
