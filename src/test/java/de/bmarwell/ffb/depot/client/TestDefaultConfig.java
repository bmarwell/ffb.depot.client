package de.bmarwell.ffb.depot.client;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import java.net.URI;
import org.junit.Test;

public class TestDefaultConfig {
  @Test
  public void testDefaultConfig() {
    final FfbDefaultConfig config = new FfbDefaultConfig();

    assertThat(config, is(not(nullValue())));
    final URI baseUrl = config.getBaseUrl();
    assertThat(baseUrl, is(not(nullValue())));
    assertThat(baseUrl.toASCIIString(), startsWith("http"));
    assertThat(baseUrl.toASCIIString(), containsString("://"));
  }
}
