package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbDepotUtils;
import de.bmarwell.ffb.depot.client.FfbMobileClient;
import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.LoginResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import com.google.common.base.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

public class TestMobileGetDepotwert {

  private static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  private static final FfbPin PIN = FfbPin.of("91901");
  private static final FfbDepotNummer DEPOTNUMMER = FfbDepotNummer.of("222223");
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

    Optional<MyFfbResponse> accountData = mobileAgent.fetchAccountData();
    Assert.assertTrue(accountData.isPresent());
    LOG.debug("Account data: [{}].", accountData.get());
    double depotBestand = FfbDepotUtils.getGesamtBestand(accountData.get(), DEPOTNUMMER);
    LOG.debug("Depotbestand: [{}].", depotBestand);

    Assert.assertTrue(depotBestand > 0.0);
    Assert.assertTrue(accountData.get().isLogin());
    Assert.assertFalse(accountData.get().isModelportfolio());
    Assert.assertTrue(accountData.get().getGesamtwert() != 0.00d);
    LOG.debug("MyFfb: [{}].", accountData.get());

    // Assert.assertTrue(mobileAgent.getDepotwert().contains(","));
    // Assert.assertTrue(mobileAgent.getDepotwert().matches("[0-9]+,[0-9]{2}"));
  }

}
