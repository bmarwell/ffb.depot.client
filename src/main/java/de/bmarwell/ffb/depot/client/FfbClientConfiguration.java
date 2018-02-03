package de.bmarwell.ffb.depot.client;

import java.net.URI;

public interface FfbClientConfiguration {

  /**
   * Domain zur Fidelity-API-Seite.
   *
   * @return die Base-URI, etwa https://www.fidelity.com.
   */
  URI getBaseUrl();

  /**
   * The userAgent to identify as (http header).
   *
   * Default: {@code ffb.depot.client}.
   *
   * @return the userAgent to be used.
   */
  default String getUserAgent() {
    return "ffb.depot.client";
  }
}
