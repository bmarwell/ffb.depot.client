package de.bmarwell.ffb.depot.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

public class JacksonMessageBodyReader implements MessageBodyReader<Object> {

  private static final ObjectMapper OM = ObjectMapperProvider.getInstance();

  @Override
  public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
    return true;
  }

  @Override
  public Object readFrom(Class<Object> aClass, Type type, Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
    return OM.readValue(inputStream, aClass);
  }
}
