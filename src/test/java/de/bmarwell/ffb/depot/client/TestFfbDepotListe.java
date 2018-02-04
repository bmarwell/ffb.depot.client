package de.bmarwell.ffb.depot.client;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 * Test DepotListe object as it extends ArrayList.
 */
public class TestFfbDepotListe {

  /**
   * Test copy method to copy references.
   */
  @Test
  public void testCopy() {
    final FfbDepotInfo depot1 = FfbDepotInfo.builder()
        .depotNummer(FfbDepotNummer.of("1"))
        .depotname("name1")
        .gesamtDepotBestand(BigDecimal.valueOf(2.00d))
        .addAllFondsbestaende(Collections.emptyList())
        .build();

    final FfbDepotInfo depot2 = FfbDepotInfo.builder()
        .depotNummer(FfbDepotNummer.of("2"))
        .depotname("name2")
        .gesamtDepotBestand(BigDecimal.valueOf(1.00d))
        .addAllFondsbestaende(Collections.emptyList())
        .build();

    final List<FfbDepotInfo> liste = unmodifiableList(asList(depot1, depot2));

    final List<FfbDepotInfo> copy = unmodifiableList(liste);

    assertTrue("Should not be the same object, only contents.", liste.equals(copy));
    assertEquals("Copy should have same interior.", liste, copy);
  }

}
