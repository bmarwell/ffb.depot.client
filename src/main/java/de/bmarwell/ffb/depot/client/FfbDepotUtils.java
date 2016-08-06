package de.bmarwell.ffb.depot.client;

import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import com.google.common.base.Preconditions;

public class FfbDepotUtils {
  private FfbDepotUtils() {
    // utility class
  }

  /**
   * Ermittelt den Gesamtbestand für ein bestimmtes Depot.
   *
   * @param myFfbResponse
   *          Das Ergebnis der {@link FfbMobileDepotwertRetriever#fetchAccountData()}-Methode.
   *
   * @param depotnummer
   *          Die Depotnummer, für die der Depotbestand abgefragt werden soll. Ein Login kann ggf. mehrere Depots sehen.
   *
   * @return der Gesamtbestand in Depotwährung.
   */
  public static double getGesamtBestand(MyFfbResponse myFfbResponse, FfbDepotNummer depotnummer) {
    Preconditions.checkNotNull(depotnummer, "Depotnummer null.");
    Preconditions.checkNotNull(myFfbResponse, "Keine Daten übergeben!");

    double tempDepotwert = 0.00d;

    /* Es kann mehrere Depots mit der gleichen Depotnummer geben (z.B. Haupt- und VL-Depot). */
    for (FfbDepotInfo di : myFfbResponse.getDepots()) {
      if (!di.getDepotnummer().equals(depotnummer.getDepotNummer())) {
        /* Dieses Depot im sichtbaren Login ist ein anderes, als das für Umsätze angefordete */
        continue;
      }

      tempDepotwert += di.getBestand();
    }

    return tempDepotwert;
  }

}
