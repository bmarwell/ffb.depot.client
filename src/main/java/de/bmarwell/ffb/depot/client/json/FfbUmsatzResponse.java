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

package de.bmarwell.ffb.depot.client.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableFfbUmsatzResponse.class)
public interface FfbUmsatzResponse {
  @JsonProperty("login")
   String getlogin();

  @JsonProperty("error")
  String getError();

  @JsonProperty("anzahlUmsaetze")
  String getAnzahlUmsaetze();

  /**
   *
   * <p>Example: &quot;/de/fonds/quick-factsheet-overlay.page?&quot;</p>
   * @return a path fragment.
   */
  @JsonProperty("urlFactsheetOverlay")
  String getUrlFactsheetOverlay();

  String getHash();

  @JsonProperty("umsaetze")
  List<FfbUmsatz> getUmsaetze();
}
