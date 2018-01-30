package de.bmarwell.ffb.depot.client;

import java.net.URI;

public class FfbDefaultConfig implements FfbClientConfiguration {

  @Override
  public URI getBaseUrl() {
    return URI.create("https://www.fidelity.de");
  }
}
