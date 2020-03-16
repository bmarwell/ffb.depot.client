/*
 *  Copyright 2018 The ffb.depot.client contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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

import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.net.URI;
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
  public void setUp() {
    final FfbClientConfiguration config = () -> URI.create("http://localhost:" + this.wiremock.port());

    this.client = new FfbMobileClient(LOGIN, PIN, config);
  }

  @Test
  public void testFondsBestand() {
    this.client.logon();
    final MyFfbResponse accountData = this.client.fetchAccountData();
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
    assertThat(fondsbestand.compareTo(fondsbestand), is(0));

    assertThat(fondsbestand.getIsin(), is(equalTo(wellKnownIsin)));
    final String fondsbestandString = fondsbestand.toString().toLowerCase(Locale.getDefault());
    assertThat(fondsbestandString, containsString("wkn"));
  }
}
