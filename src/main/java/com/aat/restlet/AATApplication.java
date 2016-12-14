package com.aat.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;


public class AATApplication extends Application {
	
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
        
		// Add endpoints
        //router.attach("/guestbook/", GuestbookResource.class);
        
        return router;
	}

}
