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

package de.bmarwell.ffb.depot.client.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.bmarwell.ffb.depot.client.util.GermanNumberToBigDecimalDeserializer;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.immutables.value.Value;

/**
 * Information about a depot. You can have multiple Depots in your login view, thus this class should be comparable/sortable.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableFfbDepotInfo.class)
public interface FfbDepotInfo extends Comparable<FfbDepotInfo> {

  /**
   * A name of th depot. Actually, it is much more like a category ("Standarddepot", "VL-Depot", etc.).
   *
   * @return the Depotname as String.
   */
  String getDepotname();

  @JsonProperty("depotnummer")
  FfbDepotNummer getDepotNummer();

  @JsonProperty("bestand")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getGesamtDepotBestand();

  /**
   * Each fund is represendet by {@link FfbFondsbestand}.
   *
   * @return a list of funds.
   */
  List<FfbFondsbestand> getFondsbestaende();

  static ImmutableFfbDepotInfo.Builder builder() {
    return ImmutableFfbDepotInfo.builder();
  }

  /**
   * Compare to other by {@link #getDepotNummer()}, {@link #getDepotname()} and {@link #getGesamtDepotBestand()}.
   */
  @Override
  default int compareTo(FfbDepotInfo other) {
    final Comparator<FfbDepotInfo> comparator = Comparator
        .comparing(FfbDepotInfo::getDepotNummer)
        .thenComparing(FfbDepotInfo::getDepotname)
        .thenComparing(FfbDepotInfo::getGesamtDepotBestand);

    return comparator.compare(this,other);
  }
}
