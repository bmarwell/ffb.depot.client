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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FFB Depot Client. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.bmarwell.ffb.depot.client;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbDisposition;
import de.bmarwell.ffb.depot.client.json.FfbDispositionenResponse;
import de.bmarwell.ffb.depot.client.json.ImmutableFfbDisposition;
import de.bmarwell.ffb.depot.client.json.ImmutableFfbDispositionenResponse;

import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Locale;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.List;

public class TestDispositionen {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TestDispositionen.class);


  private static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  private static final FfbPin PIN = FfbPin.of("91901");

  private static final FfbDepotNummer DEPOT = FfbDepotNummer.of("222223");

  @Rule
  public WireMockRule wiremock = new WireMockRule(wireMockConfig().dynamicPort());

  private FfbMobileClient client;

  @Before
  public void setUp() throws Exception {
    final FfbClientConfiguration config = () -> URI.create("http://localhost:" + wiremock.port());

    this.client = new FfbMobileClient(LOGIN, PIN, config);
  }

  @Test
  public void testGetDispositionen() throws FfbClientError, MalformedURLException {
    client.logon();
    Assert.assertTrue(client.isLoggedIn());

    FfbDispositionenResponse umsaetze = client.getDispositionen();
    Assert.assertNotNull(umsaetze);
    LOG.debug("Dispositionen: [{}].", umsaetze);
  }

  @Test
  public void testDispositionObjects() {
    final ImmutableFfbDisposition dispo1 = FfbDisposition.builder()
        .depot("depot")
        .fondsname("fondsname")
        .isin("isin")
        .wkn("wkn")
        .kagName("KAG")
        .betrag(BigDecimal.valueOf(10.50d))
        .stuecke(BigDecimal.valueOf(1d))
        .auftragtyp("auftragstyp")
        .rabattAsString(BigDecimal.valueOf(100L))
        .eingabedatum(LocalDate.now())
        .verrechnungskonto("referenzkonto")
        .teilauftragtyp("teilauftragstyp")
        .build();
    FfbDispositionenResponse dispos = ImmutableFfbDispositionenResponse.builder()
        .isLogin(true)
        .dispositionenBetrag(BigDecimal.ZERO)
        .dispositionenAnzahl(100)
        .dispositionen(asList(dispo1))
        .build();

    List<FfbDisposition> dispositionen = dispos.getDispositionen();
    Assert.assertFalse(dispositionen.isEmpty());
    Assert.assertEquals("Es sollte ein Element vorhanden sein.", 1, dispositionen.size());
    FfbDisposition firstDisposition = dispositionen.get(0);
    Assert.assertEquals("Das vorhandene Element sollte dem erstellten entsprechen.", dispo1, firstDisposition);
    assertThat(firstDisposition.toString().toLowerCase(Locale.getDefault()), CoreMatchers.containsString("fondsname"));
  }

}
