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
   * <p>Hintergrund dieser Funktion ist, dass ein Depotlogin durchaus auf mehrere FFB-Depots Zugriff hat. Diese Funktion
   * iteriert über alle Depots, die in diesem Login verfügbar sind, und ermittelt dessen Gesamtdepotwert.</p>
   *
   * @param myFfbResponse
   *          Das Ergebnis der {@link FfbMobileClient#fetchAccountData()}-Methode.
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
      if (!di.getDepotNummer().equals(depotnummer)) {
        /* Dieses Depot im sichtbaren Login ist ein anderes, als das für Umsätze angefordete */
        continue;
      }

      tempDepotwert += di.getGesamtDepotBestand();
    }

    return tempDepotwert;
  }

}
