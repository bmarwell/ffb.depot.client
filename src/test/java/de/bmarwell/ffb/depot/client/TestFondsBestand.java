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
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFondsBestand {

  private static final Logger LOG = LoggerFactory.getLogger(TestFondsBestand.class);

  private static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  private static final FfbPin PIN = FfbPin.of("91901");

  private static final FfbDepotNummer DEPOT = FfbDepotNummer.of("222223");

  @Rule
  public WireMockRule wiremock = new WireMockRule(wireMockConfig().dynamicPort());

  private FfbMobileClient client;

  @Before
  public void setUp() throws Exception {
    final FfbClientConfiguration config = () -> URI.create("http://localhost:" + wiremock.port());

    client = new FfbMobileClient(LOGIN, PIN, config);
  }

  @Test
  public void testFondsBestand() throws Exception {
    client.logon();
    final MyFfbResponse accountData = client.fetchAccountData();
    LOG.debug("AccountData: [{}].", accountData);

    final FfbDepotInfo depotinfo = accountData.getDepots().stream()
        .filter(depot -> DEPOT.equals(depot.getDepotNummer()))
        .findAny()
        .orElseThrow(IllegalStateException::new);
    LOG.debug("Depot gefunden: [{}].", depotinfo);

    final List<FfbFondsbestand> fondsbestaende = depotinfo.getFondsbestaende();
    LOG.debug("Fondsbestände: [{}].", fondsbestaende);

    assertThat("Es sollte Fondsbestände geben.", fondsbestaende, is(not(empty())));
    final Set<String> isins = fondsbestaende.stream()
        .map(FfbFondsbestand::getIsin)
        .collect(toSet());
    assertThat("Es sollte den Test-Fondsbestand geben.", isins, is(not(empty())));
    final String wellKnownIsin = "DE0008475120";
    assertThat("ISIN DE0008475120 sollte bekannt sein.", isins, hasItem(wellKnownIsin));

    final FfbFondsbestand fondsbestand = fondsbestaende.stream()
        .filter(bestand -> wellKnownIsin.equals(bestand.getIsin()))
        .findAny().orElseThrow(IllegalStateException::new);

    assertThat(fondsbestand.getIsin(), is(equalTo(wellKnownIsin)));
    assertTrue(fondsbestand.getPreisDatum().isAfter(LocalDate.now().minusDays(5)));
    final String fondsbestandString = fondsbestand.toString().toLowerCase(Locale.getDefault());
    assertThat(fondsbestandString, containsString("wkn"));
  }
}
