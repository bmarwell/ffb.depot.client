package de.bmarwell.ffb.depot.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.bmarwell.ffb.depot.client.FfbMobileClient;
import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbUmsatz;
import de.bmarwell.ffb.depot.client.json.FfbUmsatzResponse;
import de.bmarwell.ffb.depot.client.value.FfbAuftragsTyp;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUmsaetze {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TestUmsaetze.class);

  @Test
  public void testUmsaetze() throws MalformedURLException, FfbClientError {
    FfbMobileClient mobileAgent = new FfbMobileClient(TestMobileGetDepotwert.LOGIN, TestMobileGetDepotwert.PIN);
    mobileAgent.logon();
    Assert.assertTrue(mobileAgent.loginInformation().isPresent());

    FfbUmsatzResponse umsaetze = mobileAgent.getUmsaetze(FfbAuftragsTyp.ALLE, LocalDate.now().minusMonths(5).minusDays(14),
        LocalDate.now());
    LOG.debug("Umsätze erhalten: [{}].", umsaetze);

    mobileAgent.logout();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUmsaetzeBigInterval() throws MalformedURLException, FfbClientError {
    FfbMobileClient mobileAgent = new FfbMobileClient(TestMobileGetDepotwert.LOGIN, TestMobileGetDepotwert.PIN);
    mobileAgent.logon();
    Assert.assertTrue(mobileAgent.loginInformation().isPresent());

    mobileAgent.getUmsaetze(FfbAuftragsTyp.ALLE, LocalDate.now().minusMonths(15).minusDays(15), LocalDate.now());

    mobileAgent.logout();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUmsaetzeLongAgo() throws MalformedURLException, FfbClientError {
    FfbMobileClient mobileAgent = new FfbMobileClient(TestMobileGetDepotwert.LOGIN, TestMobileGetDepotwert.PIN);
    mobileAgent.logon();
    Assert.assertTrue(mobileAgent.loginInformation().isPresent());

    mobileAgent.getUmsaetze(FfbAuftragsTyp.ALLE,
        LocalDate.now().minusMonths(15).minusDays(15),
        LocalDate.now().minusMonths(14));

    mobileAgent.logout();
  }

  @Test
  public void testUmsaetzeFromJson() throws IOException {
    GsonBuilder initGsonBuilder = FfbMobileClient.initGsonBuilder();
    Gson gson = initGsonBuilder.create();

    InputStream umsatzStream = this.getClass().getResourceAsStream("/json/umsatzResponse.json");
    InputStreamReader isr = new InputStreamReader(umsatzStream);
    FfbUmsatzResponse umsatzResponse = gson.fromJson(isr, FfbUmsatzResponse.class);

    Assert.assertNotNull("Should have unmarshalled json to object.", umsatzResponse);
    List<FfbUmsatz> umsaetze = umsatzResponse.getUmsaetze();
    Assert.assertNotNull("Umsaetze should never be null, just empty list perhaps.", umsaetze);
    Assert.assertNotEquals("Umsaetze should not be zero count.", 0, umsaetze.size());

    LOG.debug("UmsatzResponse: [{}].", umsatzResponse);
  }
}
