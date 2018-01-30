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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;
import de.bmarwell.ffb.depot.client.json.LoginResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMobileGetDepotwert {

  @Rule
  public WireMockRule wiremock = new WireMockRule(wireMockConfig().dynamicPort());

  public static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  public static final FfbPin PIN = FfbPin.of("91901");
  public static final FfbDepotNummer DEPOTNUMMER = FfbDepotNummer.of("222223");
  private static final Logger LOG = LoggerFactory.getLogger(TestMobileGetDepotwert.class);
  private FfbMobileClient client;
  private FfbMobileClient clientWithoutCredentials;

  @Before
  public void setUp() throws Exception {
    final FfbClientConfiguration config = () -> URI.create("http://localhost:" + wiremock.port());

    this.client = new FfbMobileClient(LOGIN, PIN, config);
    this.clientWithoutCredentials = new FfbMobileClient(config);
  }

  @Test
  public void testGetDepotwertWithoutCredentials() throws FfbClientError, MalformedURLException {
    clientWithoutCredentials.logon();
    assertFalse(clientWithoutCredentials.isLoggedIn());
    LoginResponse loginResponse = clientWithoutCredentials.loginInformation();
    LOG.debug("Login: [{}].", loginResponse);

    assertFalse(loginResponse.isLoggedIn());
  }

  @Test
  public void testLoginLogout() throws MalformedURLException, FfbClientError {
    client.logon();

    LoginResponse loginResponse = client.loginInformation();
    LOG.debug("Login: [{}].", loginResponse);

    assertTrue("Should be logged in.", loginResponse.isLoggedIn());

    boolean logout = client.logout();
    Assert.assertThat("logout should yield success.", logout, CoreMatchers.is(true));
    assertFalse("loginInformation should be gone now.", client.isLoggedIn());
  }

  @Test(expected = IllegalStateException.class)
  public void testFfbIllegalState() throws MalformedURLException, FfbClientError {
    client.getPerformance();
  }

  /**
   * Tests with a valid test login.
   *
   * <p>Login taken from: <a href= "http://www.wertpapier-forum.de/topic/31477-demo-logins-depots-einfach-mal-testen/page__view__findpost__p__567459">
   * Wertpapier-Forum</a>.<br> Login: 22222301<br> PIN: 91901</p>
   *
   * @throws MalformedURLException Fehler beim Erstellen der URL.
   * @throws FfbClientError Errror with fetching data.
   */
  @Test
  public void testGetDepotwertWithCredentials() throws MalformedURLException, FfbClientError {
    client.logon();
    assertTrue(client.isLoggedIn());

    LoginResponse loginResponse = client.loginInformation();
    LOG.debug("Login: [{}].", loginResponse);

    assertTrue(loginResponse.isLoggedIn());
    assertEquals("Customer", loginResponse.getUsertype());
    assertEquals("E1000590054", loginResponse.getLastname());

    MyFfbResponse accountData = client.fetchAccountData();
    assertTrue(accountData != null);
    LOG.debug("Account data: [{}].", accountData);

    for (FfbDepotInfo depot : accountData.getDepots()) {
      LOG.debug("Depotinfo: [{}].", depot);
    }

    BigDecimal depotBestand = FfbDepotUtils.getGesamtBestand(accountData, DEPOTNUMMER);
    LOG.debug("Depotbestand: [{}].", depotBestand);

    assertTrue(depotBestand.compareTo(BigDecimal.ZERO) > 0);
    assertTrue(accountData.isLoggedIn());
    assertFalse(accountData.isModelportfolio());
    assertTrue(accountData.getGesamtwert().compareTo(BigDecimal.ZERO) > 0);
    LOG.debug("MyFfb: [{}].", accountData);

    // Teste Depotbestände
    assertFalse(accountData.getDepots().isEmpty());
    boolean hasDepotBestand = false;
    for (FfbDepotInfo depot : accountData.getDepots()) {
      if (depot.getFondsbestaende().isEmpty()) {
        continue;
      }

      hasDepotBestand = true;
      for (FfbFondsbestand bestand : depot.getFondsbestaende()) {
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
  public void testToString() throws MalformedURLException {
    String mobileToString = client.toString();

    assertNotNull("ToString may not be missing.", mobileToString);

    LOG.debug("FfbMobileClient: [{}]", mobileToString);

    assertTrue("User should be set.", mobileToString.contains(LOGIN.getLoginKennung()));
  }

}
