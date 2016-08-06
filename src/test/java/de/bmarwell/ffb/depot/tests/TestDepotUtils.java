package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.FfbDepotUtils;
import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

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
    FfbDepotInfo depot1 = new FfbDepotInfo();
    depot1.setDepotnummer(depotNummer);
    depot1.setBestand("1000,00");

    FfbDepotInfo depot2 = new FfbDepotInfo();
    depot2.setDepotnummer(depotNummer);
    depot2.setBestand("1000,00");

    FfbDepotNummer depotNummer2 = FfbDepotNummer.of("2");
    FfbDepotInfo depot3 = new FfbDepotInfo();
    depot3.setDepotnummer(depotNummer2);
    depot3.setBestand("1000,00");

    MyFfbResponse ffbResponse = new MyFfbResponse();
    ffbResponse.getDepots().add(depot1);
    ffbResponse.getDepots().add(depot2);
    ffbResponse.getDepots().add(depot3);

    double gesamtBestand = FfbDepotUtils.getGesamtBestand(ffbResponse, depotNummer);

    /* Depot 3 should not count into it. */
    Assert.assertEquals(2000.00, gesamtBestand, 0.10);
  }
}
