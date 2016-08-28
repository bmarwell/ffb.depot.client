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

package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbDepotUtils;
import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.FfbDepotliste;
import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;
import de.bmarwell.ffb.depot.client.json.ImmutableMyFfbResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import com.google.common.collect.ImmutableList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.LocalDate;

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
    MyFfbResponse ffbResponse = ImmutableMyFfbResponse.builder()
        .depots(new FfbDepotliste())
        .gesamtwertAsString("0.00")
        .isLoginAsString("true")
        .letztesUpdate("")
        .isModelportfolio(false)
        .build();
    double gesamtBestand = FfbDepotUtils.getGesamtBestand(ffbResponse, FfbDepotNummer.empty());

    Assert.assertEquals(0.00, gesamtBestand, 0.01);
  }

  @Test
  public void testTwoDepotsInResponse() {
    FfbDepotNummer depotNummer = FfbDepotNummer.of("1");
    FfbDepotInfo depot1 = FfbDepotInfo.of("Testdepot", depotNummer, 1000.00, ImmutableList.<FfbFondsbestand>of());
    FfbDepotInfo depot2 = FfbDepotInfo.of("Testdepot2", depotNummer, 1000.00, ImmutableList.<FfbFondsbestand>of());

    FfbDepotNummer depotNummer2 = FfbDepotNummer.of("2");
    FfbDepotInfo depot3 = FfbDepotInfo.of("Testdepot", depotNummer2, 1000.00, ImmutableList.<FfbFondsbestand>of());

    FfbDepotliste depotliste = new FfbDepotliste();
    depotliste.add(depot1);
    depotliste.add(depot2);
    depotliste.add(depot3);

    MyFfbResponse ffbResponse = ImmutableMyFfbResponse.of(true, false, "", 0.00D, depotliste);

    Assert.assertEquals(3, ffbResponse.getDepots().size());

    Assert.assertEquals(depotNummer, depot1.getDepotNummer());
    Assert.assertEquals(depotNummer, depot2.getDepotNummer());
    Assert.assertNotEquals(depotNummer, depot3.getDepotNummer());

    double gesamtBestand = FfbDepotUtils.getGesamtBestand(ffbResponse, depotNummer);

    /* Depot 3 should not count into it. */
    Assert.assertEquals(2000.00, gesamtBestand, 0.10);
  }

  @Test
  public void testConvertGermanDateWorks() {
    final String firstOfJanuary2016 = "01.01.2016";
    LocalDate germanDateToLocalDate = FfbDepotUtils.convertGermanDateToLocalDate(firstOfJanuary2016);

    Assert.assertNotNull("computed date should not be null.", germanDateToLocalDate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGermanDate() {
    final String noDate = "ab.cd.efff";
    FfbDepotUtils.convertGermanDateToLocalDate(noDate);
  }
}
