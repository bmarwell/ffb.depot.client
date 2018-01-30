package de.bmarwell.ffb.depot.client;

import java.net.URI;

public interface FfbClientConfiguration {

  /**
   * Domain zur Fidelity-API-Seite.
   *
   * @return die Base-URI, etwa https://www.fidelity.com.
   */
  URI getBaseUrl();
}
