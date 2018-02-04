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

package de.bmarwell.ffb.depot.client;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertTrue;

import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import javax.ws.rs.core.NewCookie;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCookies {

  private static final Logger LOG = LoggerFactory.getLogger(TestCookies.class);


  private static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  private static final FfbPin PIN = FfbPin.of("91901");

  @Rule
  public WireMockRule wiremock = new WireMockRule(wireMockConfig().dynamicPort());

  private FfbMobileClient client;

  @Before
  public void setUp() {
    final FfbClientConfiguration config = () -> URI.create("http://localhost:" + wiremock.port());

    this.client = new FfbMobileClient(LOGIN, PIN, config);
  }

  @Test
  public void testFfbMobileClientFfbLoginKennungFfbPin() throws FfbClientError {
    boolean sessionIdFound = false;
    client.logon();
    final Map<String, NewCookie> cookies = client.getCookies();

    for (final Entry<String, NewCookie> cookie : cookies.entrySet()) {
      /* At least one of the cookies should be a JSESSIONID. */
      if ("JSESSIONID".equals(cookie.getKey())) {
        sessionIdFound = true;
      }

      LOG.debug("Cookie: [{}].", cookie);
    }

    client.logout();
    assertTrue("SessionId has not been found -- user never logged in?", sessionIdFound);
  }

}
