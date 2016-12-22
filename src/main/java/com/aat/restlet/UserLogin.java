package com.aat.restlet;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.aat.datastore.User;
import com.googlecode.objectify.ObjectifyService;

public class UserLogin extends ServerResource{
		private final static String LOGIN = "login successfully";
		private final static String LOGOUT = "wrong password";
		private final static String NOUSER = "user does not exist";
	
	@Post
	   public String passwordCheck(){
		   UserLogin user=new UserLogin();
		   if(retrieveUser(getAttribute("id"))==null||!getAttribute("email").equals(retrieveEmail(getAttribute("id"))))
		   {
			   return NOUSER;
		   }
		   else 
		   {	if(getAttribute("password").equals(user.retrievePassword(getAttribute("id")))){
			   		return LOGIN;
		   		}
		   		else{
		   			return LOGOUT;
		   		}
		   }
	   }
	
	/**
	    * @param id
	    * @return User user
	    * Retrieves a user by id.
	    * */
	   private User retrieveUser (String id){
		   User user = ObjectifyService.ofy()
				   .load()
				   .type(User.class)
				   .id(Long.parseLong(id,10)).now();	   	   
		   return user;
	   } 


		private String retrievePassword(String id){
		   User user = retrieveUser(id);
		   return user.getPassword();
	   }   
	
		private String retrieveEmail(String id){
			User user = retrieveUser(id);
			return user.getEmail();
		}
}
