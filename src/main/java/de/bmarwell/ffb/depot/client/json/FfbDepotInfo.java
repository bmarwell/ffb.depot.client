/*
 *  Copyright 2018 The ffb.depot.client contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.bmarwell.ffb.depot.client.json;

import de.bmarwell.ffb.depot.client.util.GermanNumberToBigDecimalDeserializer;
import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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

  static ImmutableFfbDepotInfo.Builder builder() {
    return ImmutableFfbDepotInfo.builder();
  }

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

  /**
   * Compare to other by {@link #getDepotNummer()}, {@link #getDepotname()} and {@link #getGesamtDepotBestand()}.
   */
  @Override
  default int compareTo(final FfbDepotInfo other) {
    final Comparator<FfbDepotInfo> comparator = Comparator
        .comparing(FfbDepotInfo::getDepotNummer)
        .thenComparing(FfbDepotInfo::getDepotname)
        .thenComparing(FfbDepotInfo::getGesamtDepotBestand);

    return comparator.compare(this,other);
  }
}
