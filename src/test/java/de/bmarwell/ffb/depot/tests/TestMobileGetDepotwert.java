package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbMobileDepotwertRetriever;
import de.bmarwell.ffb.depot.client.LoginResponse;
import de.bmarwell.ffb.depot.client.MyFfbResponse;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestMobileGetDepotwert {

  private static final Logger LOG = LoggerFactory.getLogger(TestMobileGetDepotwert.class);

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testGetDepotwertWithoutCredentials() throws FailingHttpStatusCodeException, IOException {
    FfbMobileDepotwertRetriever mobileAgent = new FfbMobileDepotwertRetriever();
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
   */
  @Test
  public void testGetDepotwertWithCredentials() {
    FfbMobileDepotwertRetriever mobileAgent = new FfbMobileDepotwertRetriever("22222301", "91901", "22222301");
    mobileAgent.synchronize();
    Assert.assertTrue(mobileAgent.loginInformation().isPresent());

    LoginResponse loginResponse = mobileAgent.loginInformation().get();
    LOG.debug("Login: [{}].", loginResponse);

    Assert.assertTrue(loginResponse.isLoggedIn());
    Assert.assertEquals("Customer", loginResponse.getUsertype());
    Assert.assertEquals("E1000590054", loginResponse.getLastname());

    Assert.assertTrue(mobileAgent.depotInformation().isPresent());
    MyFfbResponse ffbDepotInfo = mobileAgent.depotInformation().get();
    Assert.assertTrue(ffbDepotInfo.isLogin());
    Assert.assertFalse(ffbDepotInfo.isModelportfolio());
    Assert.assertTrue(ffbDepotInfo.getGesamtwert() != 0.00d);
    LOG.debug("MyFfb: [{}].", ffbDepotInfo);

    // Assert.assertTrue(mobileAgent.getDepotwert().contains(","));
    // Assert.assertTrue(mobileAgent.getDepotwert().matches("[0-9]+,[0-9]{2}"));
  }

}
