/*
 *  Copyright 2018 The ffb.depot.client contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
  public boolean isReadable(final Class<?> aClass, final Type type, final Annotation[] annotations, final MediaType mediaType) {
    return true;
  }

  @Override
  public Object readFrom(final Class<Object> aClass, final Type type, final Annotation[] annotations, final MediaType mediaType,
      final MultivaluedMap<String, String> multivaluedMap, final InputStream inputStream) throws IOException, WebApplicationException {
    return OM.readValue(inputStream, aClass);
  }
}
