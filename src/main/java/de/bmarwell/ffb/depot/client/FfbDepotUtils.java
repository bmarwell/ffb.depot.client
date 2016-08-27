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
import de.bmarwell.ffb.depot.client.json.FfbDepotliste;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import com.google.common.base.Preconditions;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class FfbDepotUtils {
  /**
   * German date: <code>dd.MM.YYYY</code>.
   *
   * <p>I think it is kind of stupid of the FFB not to use ISO date and then convert it. This date holds no locale
   * information or whatsoever.<br><br> See also: <a href="https://xkcd.com/1179/">XKCD ISO 8601</a></p>
   */
  public static final DateTimeFormatter GERMAN_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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
    final FfbDepotliste depots = Preconditions.checkNotNull(myFfbResponse, "Keine Daten übergeben!").getDepots();

    double tempDepotwert = 0.00D;

    /* Es kann mehrere Depots mit der gleichen Depotnummer geben (z.B. Haupt- und VL-Depot). */
    for (final FfbDepotInfo di : depots) {
      if (!di.getDepotNummer().equals(depotnummer)) {
        /* Dieses Depot im sichtbaren Login ist ein anderes, als das für Umsätze angefordete */
        continue;
      }

      tempDepotwert += di.getGesamtDepotBestand();
    }

    return tempDepotwert;
  }

  public static LocalDate convertGermanDateToLocalDate(String germanDate) {
    Preconditions.checkNotNull(germanDate, "Parameter germanDate in convertGermanDateToLocalDate.");
    Preconditions.checkArgument(germanDate.matches("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}"), "Format of GermanDate != dd.mm.yyyy");

    return LocalDate.parse(germanDate, GERMAN_DATE_FORMAT);
  }

  /**
   * Converts a german number to a Java Double primitive.
   * 
   * @param germanNumber
   *          a german number format (like 1234,56 or 1.234.567,89).
   * @return a double primitive with the same value.
   * @throws NumberFormatException
   *           if the input is not a german number.
   */
  public static double convertGermanNumberToDouble(String germanNumber) {
    Preconditions.checkNotNull(germanNumber, "Parameter germanDate in convertGermanNumberToDouble.");

    return Double.parseDouble(germanNumber.replace(".", "").replace(',', '.'));
  }

}
