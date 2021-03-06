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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;
import de.bmarwell.ffb.depot.client.json.LoginResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.math.BigDecimal;
import java.net.URI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMobileGetDepotwert {

  public static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  public static final FfbPin PIN = FfbPin.of("91901");
  public static final FfbDepotNummer DEPOTNUMMER = FfbDepotNummer.of("222223");
  private static final Logger LOG = LoggerFactory.getLogger(TestMobileGetDepotwert.class);
  @Rule
  public WireMockRule wiremock = new WireMockRule(wireMockConfig().dynamicPort());
  private FfbMobileClient client;
  private FfbMobileClient clientWithoutCredentials;

  @Before
  public void setUp() {
    final FfbClientConfiguration config = () -> URI.create("http://localhost:" + wiremock.port());

    this.client = new FfbMobileClient(LOGIN, PIN, config);
    this.clientWithoutCredentials = new FfbMobileClient(config);
  }

  @Test
  public void testGetDepotwertWithoutCredentials() throws FfbClientError {
    clientWithoutCredentials.logon();
    assertFalse(clientWithoutCredentials.isLoggedIn());
    final LoginResponse loginResponse = clientWithoutCredentials.loginInformation();
    LOG.debug("Login: [{}].", loginResponse);

    assertFalse(loginResponse.isLoggedIn());
  }

  @Test
  public void testLoginLogout() throws FfbClientError {
    client.logon();

    final LoginResponse loginResponse = client.loginInformation();
    LOG.debug("Login: [{}].", loginResponse);

    assertTrue("Should be logged in.", loginResponse.isLoggedIn());

    final boolean logout = client.logout();
    Assert.assertThat("logout should yield success.", logout, is(true));
    assertFalse("loginInformation should be gone now.", client.isLoggedIn());
  }

  @Test(expected = IllegalStateException.class)
  public void testFfbIllegalState() throws FfbClientError {
    client.getPerformance();
  }

  /**
   * Tests with a valid test login.
   *
   * <p>Login taken from: <a href= "http://www.wertpapier-forum.de/topic/31477-demo-logins-depots-einfach-mal-testen/page__view__findpost__p__567459">
   * Wertpapier-Forum</a>.<br> Login: 22222301<br> PIN: 91901</p>
   *
   * @throws FfbClientError
   *     Errror with fetching data.
   */
  @Test
  public void testGetDepotwertWithCredentials() throws FfbClientError {
    client.logon();
    assertTrue(client.isLoggedIn());

    final LoginResponse loginResponse = client.loginInformation();
    LOG.debug("Login: [{}].", loginResponse);

    assertTrue(loginResponse.isLoggedIn());
    assertEquals("Customer", loginResponse.getUsertype());
    assertEquals("E1000590054", loginResponse.getLastname());

    final MyFfbResponse accountData = client.fetchAccountData();
    assertTrue(accountData != null);
    LOG.debug("Account data: [{}].", accountData);

    for (final FfbDepotInfo depot : accountData.getDepots()) {
      LOG.debug("Depotinfo: [{}].", depot);
      assertThat(depot.compareTo(depot), is(0));
    }

    final BigDecimal depotBestand = FfbDepotUtils.getGesamtBestand(accountData, DEPOTNUMMER);
    LOG.debug("Depotbestand: [{}].", depotBestand);

    assertTrue(depotBestand.compareTo(BigDecimal.ZERO) > 0);
    assertTrue(accountData.isLoggedIn());
    assertFalse(accountData.isModelportfolio());
    assertTrue(accountData.getGesamtwert().compareTo(BigDecimal.ZERO) > 0);
    LOG.debug("MyFfb: [{}].", accountData);

    // Teste Depotbestände
    assertFalse(accountData.getDepots().isEmpty());
    boolean hasDepotBestand = false;
    for (final FfbDepotInfo depot : accountData.getDepots()) {
      if (depot.getFondsbestaende().isEmpty()) {
        continue;
      }

      hasDepotBestand = true;
      for (final FfbFondsbestand bestand : depot.getFondsbestaende()) {
        assertNotNull(bestand);

        assertNotEquals(BigDecimal.ZERO, bestand.getBestandStueckzahl());
        assertNotEquals(BigDecimal.ZERO, bestand.getBestandWertInEuro());
        assertNotEquals(BigDecimal.ZERO, bestand.getBestandWertInFondswaehrung());

        assertNotNull(bestand.getFondsname());
        assertNotNull(bestand.getIsin());
        assertNotNull(bestand.getWkn());
      }
    }

    assertTrue(hasDepotBestand);
  }

  @Test
  public void testToString() {
    final String mobileToString = client.toString();

    assertNotNull("ToString may not be missing.", mobileToString);

    LOG.debug("FfbMobileClient: [{}]", mobileToString);

    assertTrue("User should be set.", mobileToString.contains(LOGIN.getLoginKennung()));
  }

}
