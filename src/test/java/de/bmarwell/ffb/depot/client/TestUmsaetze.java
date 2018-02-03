package de.bmarwell.ffb.depot.client;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbUmsatzResponse;
import de.bmarwell.ffb.depot.client.value.FfbAuftragsTyp;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.net.URI;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUmsaetze {

  public static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  public static final FfbPin PIN = FfbPin.of("91901");
  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TestUmsaetze.class);

  @Rule
  public WireMockRule wiremock = new WireMockRule(wireMockConfig().dynamicPort());

  private FfbMobileClient client;

  @Before
  public void setUp() {
    final FfbClientConfiguration config = () -> URI.create("http://localhost:" + wiremock.port());

    this.client = new FfbMobileClient(LOGIN, PIN, config);
  }

  @Test
  public void testUmsaetze() throws FfbClientError {
    client.logon();
    Assert.assertTrue(client.isLoggedIn());

    final FfbUmsatzResponse umsaetze = client.getUmsaetze(FfbAuftragsTyp.ALLE, LocalDate.now().minusMonths(5).minusDays(0),
        LocalDate.now());
    LOG.debug("Umsätze erhalten: [{}].", umsaetze);

    client.logout();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUmsaetzeBigInterval() throws FfbClientError {
    client.logon();
    Assert.assertTrue(client.isLoggedIn());

    client.getUmsaetze(FfbAuftragsTyp.ALLE, LocalDate.now().minusMonths(15).minusDays(15), LocalDate.now());

    client.logout();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUmsaetzeLongAgo() throws FfbClientError {
    client.logon();
    Assert.assertTrue(client.isLoggedIn());

    client.getUmsaetze(FfbAuftragsTyp.ALLE,
        LocalDate.now().minusMonths(15).minusDays(15),
        LocalDate.now().minusMonths(14));

    client.logout();
  }

}
