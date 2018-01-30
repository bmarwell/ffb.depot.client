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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import de.bmarwell.ffb.depot.client.FfbDepotUtils;
import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;
import de.bmarwell.ffb.depot.client.json.ImmutableFfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.ImmutableMyFfbResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.util.GermanDateToLocalDateDeserializer;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDepotUtils {

  @Before
  public void setUp() throws Exception {
  }

  @Test(expected = NullPointerException.class)
  public void testNullValues() {
    FfbDepotUtils.getGesamtBestand(null, null);
  }

  @Test
  public void testEmptyResponse() {
    MyFfbResponse ffbResponse = MyFfbResponse.builder()
        .depots(emptyList())
        .gesamtwert(BigDecimal.ZERO)
        .isLoggedIn(true)
        .letztesUpdate(LocalDate.now())
        .isModelportfolio(false)
        .build();
    BigDecimal gesamtBestand = FfbDepotUtils.getGesamtBestand(ffbResponse, FfbDepotNummer.empty());

    assertEquals(BigDecimal.ZERO, gesamtBestand);
  }

  @Test
  public void testTwoDepotsInResponse() {
    FfbDepotNummer depotNummer = FfbDepotNummer.of("1");

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

    FfbDepotNummer depotNummer2 = FfbDepotNummer.of("2");
    FfbDepotInfo depot3 = ImmutableFfbDepotInfo.copyOf(depot2).withDepotNummer(depotNummer2);

    final List<FfbDepotInfo> depotListe = asList(depot1, depot2, depot3);

    MyFfbResponse ffbResponse = ImmutableMyFfbResponse.builder()
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

    BigDecimal gesamtBestand = FfbDepotUtils.getGesamtBestand(ffbResponse, depotNummer);

    /* Depot 3 should not count into it. */
    assertEquals(BigDecimal.valueOf(2000.00), gesamtBestand);
  }
}
