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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FFB Depot Client. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.bmarwell.ffb.depot.client;

import static de.bmarwell.ffb.depot.client.util.GermanDateToLocalDateDeserializer.GERMAN_DATE_FORMAT;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

import de.bmarwell.ffb.depot.client.json.FfbDepotInfo;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;


/**
 * This class has various utilities needed especially for FFB depots.
 *
 * <p>The json api will give german numbers and dates, so there are two internal conversion methods.
 * There can be multiple depots with the same number, so we provide a method to get the sum of all
 * depots.</p>
 */
public final class FfbDepotUtils {

  /**
   * Logger.
   */
  private static final Logger LOG = getLogger(FfbDepotUtils.class);

  private FfbDepotUtils() {
    // utility class
  }

  /**
   * Ermittelt den Gesamtbestand für ein bestimmtes Depot.
   *
   * <p>Hintergrund dieser Funktion ist, dass ein Depotlogin durchaus auf mehrere FFB-Depots Zugriff
   * hat. Diese Funktion iteriert über alle Depots, die in diesem Login verfügbar sind, und
   * ermittelt dessen Gesamtdepotwert.</p>
   *
   * @param myFfbResponse Das Ergebnis der {@link FfbMobileClient#fetchAccountData()}-Methode.
   * @param depotnummer Die Depotnummer, für die der Depotbestand abgefragt werden soll. Ein Login
   * kann ggf. mehrere Depots sehen.
   * @return der Gesamtbestand in Depotwährung.
   */
  public static BigDecimal getGesamtBestand(final MyFfbResponse myFfbResponse, final FfbDepotNummer depotnummer) {
    requireNonNull(depotnummer, "depotnummer in getGesamtBestand");
    final List<FfbDepotInfo> depots = requireNonNull(myFfbResponse, "Keine Daten übergeben!")
        .getDepots();

    final BigDecimal sum = depots.stream()
        .filter(depot -> depotnummer.equals(depot.getDepotNummer()))
        .map(FfbDepotInfo::getGesamtDepotBestand)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    LOG.debug("Summe aller Depotbestände mit Depotnummer [{}] = [{}].", depotnummer, sum);

    return sum;
  }


  public static String convertDateRangeToGermanDateRangeString(final LocalDate from, final LocalDate until) {
    requireNonNull(from, "from is null");
    requireNonNull(until, "from is null");

    if (!from.isBefore(until)) {
      throw new IllegalStateException("from must be before until!");
    }

    return String.format("%s+-+%s",
        from.format(GERMAN_DATE_FORMAT),
        until.format(GERMAN_DATE_FORMAT));
  }

}
