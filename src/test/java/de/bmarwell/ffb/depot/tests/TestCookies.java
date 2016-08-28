package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbMobileClient;
import de.bmarwell.ffb.depot.client.err.FfbClientError;

import com.gargoylesoftware.htmlunit.util.Cookie;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Set;

public class TestCookies {

  private static final Logger LOG = LoggerFactory.getLogger(TestCookies.class);

  @Test
  public void testFfbMobileClientFfbLoginKennungFfbPin() throws MalformedURLException, FfbClientError {
    boolean sessionIdFound = false;
    final FfbMobileClient client = new FfbMobileClient(TestMobileGetDepotwert.LOGIN, TestMobileGetDepotwert.PIN);
    client.logon();
    Set<Cookie> cookies = client.currentCookies();

    for (Cookie cookie : cookies) {
      /* At least one of the cookies should be a JSESSIONID. */
      if ("JSESSIONID".equals(cookie.getName())) {
        sessionIdFound = true;
      }
      LOG.debug("Cookie: [{}].", cookie);
    }

    client.logout();
    Assert.assertTrue("SessionId has not been found -- user never logged in?", sessionIdFound);
  }

}
