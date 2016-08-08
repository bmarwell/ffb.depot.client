package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbDepotUtils;
import de.bmarwell.ffb.depot.client.FfbMobileClient;
import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;
import de.bmarwell.ffb.depot.client.json.LoginResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

public class TestMobileGetDepotwert {

  public static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  public static final FfbPin PIN = FfbPin.of("91901");
  public static final FfbDepotNummer DEPOTNUMMER = FfbDepotNummer.of("222223");
  private static final Logger LOG = LoggerFactory.getLogger(TestMobileGetDepotwert.class);

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testGetDepotwertWithoutCredentials() throws FfbClientError, MalformedURLException {
    FfbMobileClient mobileAgent = new FfbMobileClient();
    mobileAgent.logon();

    Assert.assertTrue(mobileAgent.loginInformation().isPresent());
    LoginResponse loginResponse = mobileAgent.loginInformation().get();
    LOG.debug("Login: [{}].", loginResponse);

    Assert.assertFalse(loginResponse.isLoggedIn());
  }

  /**
   * Tests with a valid test login.
   *
   * <p>Login taken from: <a href=
   * "http://www.wertpapier-forum.de/topic/31477-demo-logins-depots-einfach-mal-testen/page__view__findpost__p__567459">
   * Wertpapier-Forum</a>.<br> Login: 22222301<br> PIN: 91901</p>
   *
   * @throws MalformedURLException
   *           Fehler beim Erstellen der URL.
   * @throws FfbClientError
   *           Errror with fetching data.
   */
  @Test
  public void testGetDepotwertWithCredentials() throws MalformedURLException, FfbClientError {
    FfbMobileClient mobileAgent = new FfbMobileClient(LOGIN, PIN);
    mobileAgent.logon();
    Assert.assertTrue(mobileAgent.loginInformation().isPresent());

    LoginResponse loginResponse = mobileAgent.loginInformation().get();
    LOG.debug("Login: [{}].", loginResponse);

    Assert.assertTrue(loginResponse.isLoggedIn());
    Assert.assertEquals("Customer", loginResponse.getUsertype());
    Assert.assertEquals("E1000590054", loginResponse.getLastname());

    MyFfbResponse accountData = mobileAgent.fetchAccountData();
    Assert.assertTrue(accountData != null);
    LOG.debug("Account data: [{}].", accountData);
    double depotBestand = FfbDepotUtils.getGesamtBestand(accountData, DEPOTNUMMER);
    LOG.debug("Depotbestand: [{}].", depotBestand);

    Assert.assertTrue(depotBestand > 0.0);
    Assert.assertTrue(accountData.isLogin());
    Assert.assertFalse(accountData.isModelportfolio());
    Assert.assertTrue(accountData.getGesamtwert() != 0.00d);
    LOG.debug("MyFfb: [{}].", accountData);

    // Teste Depotbestände
    Assert.assertFalse(accountData.getDepots().isEmpty());
    boolean hasDepotBestand = false;
    for (FfbDepotInfo depot : accountData.getDepots()) {
      if (depot.getFondsbestaende().isEmpty()) {
        continue;
      }

      hasDepotBestand = true;
      for (FfbFondsbestand bestand : depot.getFondsbestaende()) {
        Assert.assertNotNull(bestand);
        Assert.assertNotEquals(0.00, bestand.getBestandStueckzahl(), 0.1);
        Assert.assertNotEquals(0.00, bestand.getBestandWertInEuro(), 0.1);
        Assert.assertNotEquals(0.00, bestand.getBestandWertInFondswaehrung(), 0.1);
        Assert.assertNotNull(bestand.getFondsname());
        Assert.assertNotNull(bestand.getIsin());
        Assert.assertNotNull(bestand.getWkn());
      }
    }

  }

}
