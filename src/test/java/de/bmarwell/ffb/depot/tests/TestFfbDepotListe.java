package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.FfbDepotliste;
import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import com.google.common.collect.ImmutableList;

import org.junit.Assert;
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
    FfbDepotInfo depot1 = FfbDepotInfo.of("name1", FfbDepotNummer.of("1"), 2.00, ImmutableList.<FfbFondsbestand>of());
    FfbDepotInfo depot2 = FfbDepotInfo.of("name2", FfbDepotNummer.of("2"), 1.00, ImmutableList.<FfbFondsbestand>of());

    FfbDepotliste liste = FfbDepotliste.of(new FfbDepotInfo[] { depot1, depot2 });
    FfbDepotliste copy = liste.copy();

    Assert.assertTrue("Should not be the same object, only contents.", liste != copy);
    Assert.assertEquals("Copy should have same interior.", liste, copy);
  }

}
