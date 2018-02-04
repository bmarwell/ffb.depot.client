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

package de.bmarwell.ffb.depot.client.value;

import de.bmarwell.ffb.depot.client.FfbMobileClient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Comparator;
import org.immutables.value.Value;

/**
 * Dieses Objekt hält eine FFB-Depotnummer.
 *
 * <p>Ein gesondertes Objekt gegenüber der {@link FfbLoginKennung} ist notwendig, da die Loginkennung geändert werden kann.
 * Die Depotnummer enthält oftmals nicht das <i>-01</i>-Suffix, wird von der FFB fest vergeben und kann nicht geändert
 * werden.</p>
 */
@Value.Immutable
public interface FfbDepotNummer extends Comparable<FfbDepotNummer> {

  /**
   * Erstellt eine Depotnummer als Immutable Objekt, die dem Konstruktor {@link FfbMobileClient} übergeben werden kann.
   *
   * @param depotnummer
   *     Die Depotnummer als String.
   *
   *     <p><b>Hinweis:</b> Sie muss nicht gleich mit dem Login sein. Der Standard-Login hat noch ein <i>-01</i> als
   *     Suffix, oder wurde vom Benutzer in etwas ganz anderes geändert. Die Depotnummer hingegen wurde von der FFB
   *     vergeben und kann nicht geändert werden.</p>
   * @return ein Depotnummer-Objekt, immutable.
   */
  @JsonCreator
  static FfbDepotNummer of(final String depotnummer) {
    return ImmutableFfbDepotNummer.of(depotnummer);
  }

  static FfbDepotNummer empty() {
    return of("");
  }

  @Value.Parameter
  @JsonValue
  String getDepotNummer();

  @Override
  default int compareTo(final FfbDepotNummer other) {
    final Comparator<FfbDepotNummer> comparator = Comparator.comparing(FfbDepotNummer::getDepotNummer);

    return comparator.compare(this, other);
  }

}
