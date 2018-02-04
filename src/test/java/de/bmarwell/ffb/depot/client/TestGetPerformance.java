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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbPerformanceResponse;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestGetPerformance {

  private static final Logger LOG = LoggerFactory.getLogger(TestGetPerformance.class);

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
  public void testGetPerformance() throws FfbClientError {
    client.logon();
    assertTrue(client.isLoggedIn());

    LOG.debug("Performance: [{}].", client.loginInformation().toString());

    final FfbPerformanceResponse performance = client.getPerformance();
    LOG.debug("Performance: [{}].", performance.toString());

    /* Die Felder mit Umwandlungen testen */
    assertTrue(performance.isLogin());
    assertEquals(LocalDate.of(2013, 12, 2), performance.getErsterZufluss());
    assertTrue(performance.getPerformanceDurchschnitt().compareTo(BigDecimal.ZERO) > 0);
    assertTrue(performance.getPerformanceGesamt().compareTo(BigDecimal.ZERO) > 0);
    assertFalse("", performance.getErrormessage().isPresent());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetPerformance_noLogin() throws FfbClientError {
    final FfbPerformanceResponse performance = client.getPerformance();
    LOG.debug("Performance: [{}].", performance.toString());
  }

}
