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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.ImmutableFfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.ImmutableMyFfbResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.Test;

public class TestDepotUtils {

  @Test(expected = NullPointerException.class)
  public void testNullValues() {
    FfbDepotUtils.getGesamtBestand(null, null);
  }

  @Test
  public void testEmptyResponse() {
    final MyFfbResponse ffbResponse = MyFfbResponse.builder()
        .depots(emptyList())
        .gesamtwert(BigDecimal.ZERO)
        .isLoggedIn(true)
        .letztesUpdate(LocalDate.now())
        .isModelportfolio(false)
        .build();
    final BigDecimal gesamtBestand = FfbDepotUtils.getGesamtBestand(ffbResponse, FfbDepotNummer.empty());

    assertEquals(BigDecimal.ZERO, gesamtBestand);
  }

  @Test
  public void testTwoDepotsInResponse() {
    final FfbDepotNummer depotNummer = FfbDepotNummer.of("1");

    final FfbDepotInfo depot1 = FfbDepotInfo.builder()
        .depotname("Testdepot")
        .depotNummer(depotNummer)
        .gesamtDepotBestand(BigDecimal.valueOf(1000.00))
        .build();
    final FfbDepotInfo depot2 = FfbDepotInfo.builder()
        .depotname("Testdepot2")
        .depotNummer(depotNummer)
        .gesamtDepotBestand(BigDecimal.valueOf(1000.00))
        .build();

    final FfbDepotNummer depotNummer2 = FfbDepotNummer.of("2");
    final FfbDepotInfo depot3 = ImmutableFfbDepotInfo.copyOf(depot2).withDepotNummer(depotNummer2);

    final List<FfbDepotInfo> depotListe = asList(depot1, depot2, depot3);

    final MyFfbResponse ffbResponse = ImmutableMyFfbResponse.builder()
        .isLoggedIn(true)
        .depots(depotListe)
        .gesamtwert(BigDecimal.ZERO)
        .letztesUpdate(LocalDate.now())
        .isModelportfolio(false)
        .build();

    assertEquals(3, ffbResponse.getDepots().size());

    assertEquals(depotNummer, depot1.getDepotNummer());
    assertEquals(depotNummer, depot2.getDepotNummer());
    assertNotEquals(depotNummer, depot3.getDepotNummer());

    final BigDecimal gesamtBestand = FfbDepotUtils.getGesamtBestand(ffbResponse, depotNummer);

    /* Depot 3 should not count into it. */
    assertEquals(BigDecimal.valueOf(2000.00), gesamtBestand);
  }
}
