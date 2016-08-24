package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbMobileClient;
import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbUmsatzResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

public class TestDispositionen {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TestDispositionen.class);

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testGetPerformance() throws FfbClientError, MalformedURLException {
    FfbMobileClient mobileAgent = new FfbMobileClient(TestMobileGetDepotwert.LOGIN, TestMobileGetDepotwert.PIN);
    mobileAgent.logon();
    Assert.assertTrue(mobileAgent.loginInformation().isPresent());

    FfbUmsatzResponse umsaetze = mobileAgent.getUmsaetze();
    Assert.assertNotNull(umsaetze);
    LOG.debug("Umsätze: [{}].", umsaetze);
  }

}
