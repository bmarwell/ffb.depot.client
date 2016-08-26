package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbMobileClient;
import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbPerformanceResponse;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.LocalDate;

import java.net.MalformedURLException;

public class TestGetPerformance {

  private static final Logger LOG = LoggerFactory.getLogger(TestGetPerformance.class);

  @Test
  public void testGetPerformance() throws FfbClientError, MalformedURLException {
    FfbMobileClient mobileAgent = new FfbMobileClient(TestMobileGetDepotwert.LOGIN, TestMobileGetDepotwert.PIN);
    mobileAgent.logon();
    Assert.assertTrue(mobileAgent.loginInformation().isPresent());

    LOG.debug("Performance: [{}].", mobileAgent.loginInformation().toString());

    FfbPerformanceResponse performance = mobileAgent.getPerformance();
    LOG.debug("Performance: [{}].", performance.toString());

    /* Die Felder mit Umwandlungen testen */
    Assert.assertTrue(performance.isLogin());
    Assert.assertEquals(LocalDate.of(2013, 12, 2), performance.getErsterZufluss());
    Assert.assertTrue(performance.getPerformanceDurchschnitt() != 0.00);
    Assert.assertTrue(performance.getPerformanceGesamt() != 0.00);
    Assert.assertEquals("", performance.getErrormessage());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetPerformance_noLogin() throws MalformedURLException, FfbClientError {
    FfbMobileClient mobileAgent = new FfbMobileClient(TestMobileGetDepotwert.LOGIN, TestMobileGetDepotwert.PIN);

    FfbPerformanceResponse performance = mobileAgent.getPerformance();
    LOG.debug("Performance: [{}].", performance.toString());
  }

}
