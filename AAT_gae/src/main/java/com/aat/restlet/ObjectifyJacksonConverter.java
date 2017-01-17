package com.aat.restlet;

import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

public class ObjectifyJacksonConverter extends JacksonConverter {
	protected <T> JacksonRepresentation<T> create(MediaType mediaType, T source) {
		return new ObjectifyJacksonRepresentation<T>(mediaType, source);
	}

	protected <T> JacksonRepresentation<T> create(Representation source, Class<T> objectClass) {
		return new ObjectifyJacksonRepresentation<T>(source, objectClass);
	}
}
