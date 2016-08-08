package de.bmarwell.ffb.depot.tests;

import de.bmarwell.ffb.depot.client.json.FfbFondsbestand;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import java.io.InputStream;
import java.io.InputStreamReader;

public class TestFondsBestand {

  @Test
  public void testFondsBestand() {
    InputStream bestandJson = this.getClass().getResourceAsStream("/json/bestand.json");
    InputStreamReader isr = new InputStreamReader(bestandJson);
    Gson gson = new Gson();
    FfbFondsbestand fondsbestand = gson.fromJson(isr, FfbFondsbestand.class);

    Assert.assertNotNull(fondsbestand);
    Assert.assertTrue(fondsbestand.getPreisDatum().isAfter(LocalDate.of(2000, 01, 01)));
    Assert.assertTrue(fondsbestand.getPreisDatum().isBefore(LocalDate.now()));
    Assert.assertTrue(fondsbestand.getRuecknahmepreis() > 0.00);
    Assert.assertTrue(fondsbestand.getBestandStueckzahl() > 0.00);
    Assert.assertTrue(fondsbestand.getBestandWertInEuro() > 0.00);
    Assert.assertTrue(fondsbestand.getBestandWertInFondswaehrung() > 0.00);
    Assert.assertEquals("Allianz Informationstechnologie", fondsbestand.getFondsname());
    Assert.assertEquals("MSCI ACWI/INFORMATION TECH STRD USD", fondsbestand.getBenchmarkName());
    Assert.assertEquals("EUR", fondsbestand.getFondswaehrung());
  }
}
