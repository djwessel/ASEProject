package com.aat.restlet;

import org.restlet.data.Form;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.aat.datastore.User;
import com.aat.utils.ResourceUtil;
import com.googlecode.objectify.ObjectifyService;

public class UserLogin extends ServerResource{
		private final static String LOGIN = "login successfully";
		private final static String LOGOUT = "wrong password";
		private final static String NOUSER = "user does not exist";
	
	@Post
	   public String passwordCheck(){
		   UserLogin user=new UserLogin();
		   Form params = getQuery();
		   String email = ResourceUtil.getParam(params, "email", true);
		   String password = ResourceUtil.getParam(params, "password", true);
		   if(retrieveUser(getAttribute("id"))==null||!email.equals(retrieveEmail(getAttribute("id"))))
		   {
			   return NOUSER;
		   }
		   else 
		   {	if(password.equals(user.retrievePassword(getAttribute("id")))){
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
