// Following the Customizing the JacksonConverter of Reslet blog
// http://restlet.com/blog/2016/03/23/customizing-the-jackson-converter-of-restlet-framework/
package com.aat.restlet;

import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectifyJacksonRepresentation<T> extends JacksonRepresentation<T> {
	public ObjectifyJacksonRepresentation(MediaType mediaType, T object) {
		super(mediaType, object);
	}

	public ObjectifyJacksonRepresentation(Representation representation, Class<T> objectClass) {
		super(representation, objectClass);
	}

	public ObjectifyJacksonRepresentation(T object) {
		super(object);
	}

	@Override
	protected ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = super.createObjectMapper();
		objectMapper.registerModule(new ObjectifyJacksonModule());

		return objectMapper;
	}
}
