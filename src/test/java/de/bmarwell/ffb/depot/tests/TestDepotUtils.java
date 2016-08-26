package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbDepotUtils;
import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import com.google.common.collect.ImmutableList;

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
    MyFfbResponse ffbResponse = new MyFfbResponse();
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

    MyFfbResponse ffbResponse = new MyFfbResponse();
    ffbResponse.getDepots().add(depot1);
    ffbResponse.getDepots().add(depot2);
    ffbResponse.getDepots().add(depot3);

    double gesamtBestand = FfbDepotUtils.getGesamtBestand(ffbResponse, depotNummer);

    /* Depot 3 should not count into it. */
    Assert.assertEquals(2000.00, gesamtBestand, 0.10);
  }
}
