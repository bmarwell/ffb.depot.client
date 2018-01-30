/*
 * (c) Copyright 2016 FFB Depot Client Developers.
 *
 * This file is part of FFB Depot Client.
 *
 * FFB Depot Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * FFB Depot Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FFB Depot Client.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.bmarwell.ffb.depot.client;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbPerformanceResponse;

import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

public class TestGetPerformance {

  private static final Logger LOG = LoggerFactory.getLogger(TestGetPerformance.class);

  private static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  private static final FfbPin PIN = FfbPin.of("91901");

  @Rule
  public WireMockRule wiremock = new WireMockRule(wireMockConfig().dynamicPort());

  private FfbMobileClient client;

  @Before
  public void setUp() throws Exception {
    final FfbClientConfiguration config = () -> URI.create("http://localhost:" + wiremock.port());

    this.client = new FfbMobileClient(LOGIN, PIN, config);
  }

  @Test
  public void testGetPerformance() throws FfbClientError, MalformedURLException {
    client.logon();
    assertTrue(client.isLoggedIn());

    LOG.debug("Performance: [{}].", client.loginInformation().toString());

    FfbPerformanceResponse performance = client.getPerformance();
    LOG.debug("Performance: [{}].", performance.toString());

    /* Die Felder mit Umwandlungen testen */
    assertTrue(performance.isLogin());
    assertEquals(LocalDate.of(2013, 12, 2), performance.getErsterZufluss());
    assertTrue(performance.getPerformanceDurchschnitt().compareTo(BigDecimal.ZERO) > 0);
    assertTrue(performance.getPerformanceGesamt().compareTo(BigDecimal.ZERO) > 0);
    assertFalse("", performance.getErrormessage().isPresent());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetPerformance_noLogin() throws MalformedURLException, FfbClientError {
    FfbPerformanceResponse performance = client.getPerformance();
    LOG.debug("Performance: [{}].", performance.toString());
  }

}
