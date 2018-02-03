package de.bmarwell.ffb.depot.client.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Singleton
 */
public enum ObjectMapperProvider {
  INSTANCE;

  private final ObjectMapper objectMapper = new ObjectMapper()
      .findAndRegisterModules()
      .registerModule(new Jdk8Module())
      .registerModule(new JavaTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

  ObjectMapperProvider() {
    // empty
  }

  public static ObjectMapper getInstance() {
    return INSTANCE.objectMapper;
  }
}
