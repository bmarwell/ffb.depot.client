package de.bmarwell.ffb.depot.client.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton
 */
public enum ObjectMapperProvider {
  INSTANCE;

  private ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

  private ObjectMapperProvider() {
    // empty
  }

  static ObjectMapper getInstance() {
    return INSTANCE.objectMapper;
  }
}
