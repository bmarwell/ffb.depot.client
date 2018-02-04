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
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbDisposition;
import de.bmarwell.ffb.depot.client.json.FfbDispositionenResponse;
import de.bmarwell.ffb.depot.client.json.ImmutableFfbDisposition;
import de.bmarwell.ffb.depot.client.json.ImmutableFfbDispositionenResponse;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDispositionen {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TestDispositionen.class);


  private static final FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
  private static final FfbPin PIN = FfbPin.of("91901");

  @Rule
  public WireMockRule wiremock = new WireMockRule(wireMockConfig().dynamicPort());

  private FfbMobileClient client;

  @Before
  public void setUp() {
    final FfbClientConfiguration config = () -> URI.create("http://localhost:" + this.wiremock.port());

    this.client = new FfbMobileClient(LOGIN, PIN, config);
  }

  @Test
  public void testGetDispositionen() throws FfbClientError {
    client.logon();
    assertTrue(client.isLoggedIn());

    final FfbDispositionenResponse umsaetze = client.getDispositionen();
    assertNotNull(umsaetze);
    LOG.debug("Dispositionen: [{}].", umsaetze);
  }

  @Test
  public void testDispositionObjects() {
    final FfbDisposition dispo1 = FfbDisposition.builder()
        .depot("depot")
        .fondsname("fondsname")
        .isin("isin")
        .wkn("wkn")
        .kagName("KAG")
        .betrag(BigDecimal.valueOf(10.50d))
        .stuecke(BigDecimal.valueOf(1d))
        .auftragtyp("auftragstyp")
        .rabatt(BigDecimal.valueOf(100L))
        .eingabedatum(LocalDate.now())
        .verrechnungskonto("referenzkonto")
        .teilauftragtyp("teilauftragstyp")
        .build();
    final FfbDisposition dispo2 = ImmutableFfbDisposition.copyOf(dispo1).withRabatt(new BigDecimal("50.0"));
    final FfbDispositionenResponse dispos = ImmutableFfbDispositionenResponse.builder()
        .isLogin(true)
        .dispositionenBetrag(BigDecimal.ZERO)
        .dispositionenAnzahl(100)
        .dispositionen(asList(dispo1, dispo2))
        .build();

    final List<FfbDisposition> dispositionen = dispos.getDispositionen();
    assertFalse(dispositionen.isEmpty());
    assertEquals("Es solltem zwei Elemente vorhanden sein.", 2, dispositionen.size());
    final FfbDisposition firstDisposition = dispositionen.get(0);
    assertEquals("Das vorhandene Element sollte dem erstellten entsprechen.", dispo1, firstDisposition);
    assertThat(firstDisposition.toString().toLowerCase(Locale.getDefault()), CoreMatchers.containsString("fondsname"));

    assertThat("sollten nicht gleich sein", dispo1.compareTo(dispo2), is(not(0)));
  }

}
