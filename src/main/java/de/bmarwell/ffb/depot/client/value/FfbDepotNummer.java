package de.bmarwell.ffb.depot.client.value;

import de.bmarwell.ffb.depot.client.FfbMobileClient;

/**
 * Dieses Objekt hält eine FFB-Depotnummer.
 *
 * <p>Ein gesondertes Objekt gegenüber der {@link FfbLoginKennung} ist notwendig, da die Loginkennung geändert werden kann.
 * Die Depotnummer enthält oftmals nicht das <i>-01</i>-Suffix, wird von der FFB fest vergeben und kann nicht geändert
 * werden.</p>
 */
public class FfbDepotNummer {

  private String depotnummer;

  private FfbDepotNummer(String depotnummer) {
    this.depotnummer = depotnummer;
  }

  /**
   * Erstellt eine Depotnummer als Immutable Objekt, die dem Konstruktor {@link FfbMobileClient} übergeben werden kann.
   *
   * @param depotnummer
   *          Die Depotnummer als String.
   *
   *          <p><b>Hinweis:</b> Sie muss nicht gleich mit dem Login sein. Der Standard-Login hat noch ein <i>-01</i> als
   *          Suffix, oder wurde vom Benutzer in etwas ganz anderes geändert. Die Depotnummer hingegen wurde von der FFB
   *          vergeben und kann nicht geändert werden.</p>
   * @return ein Depotnummer-Objekt, immutable.
   */
  public static FfbDepotNummer of(String depotnummer) {
    return new FfbDepotNummer(depotnummer);
  }

  public String getDepotNummer() {
    return this.depotnummer;
  }

  public static FfbDepotNummer empty() {
    return of("");
  }
}
