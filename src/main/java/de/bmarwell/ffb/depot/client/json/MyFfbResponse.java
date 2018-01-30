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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Der FFB-Response zur internen Seite &quot;MyFFB.page&quot;.
 *
 * <p>Leider ist die Benennung seitens der FFB nicht sonderlich glücklich, denn dieses Objekt enthält wirklich alle wichtigen
 * Daten des FFB-Kontos.</p>
 */
@Value.Immutable
@JsonSerialize(as = ImmutableFfbDepotInfo.class)
public interface MyFfbResponse {

  @JsonProperty("login")
  String isLoginAsString();

  @JsonProperty("modelportfolio")
  boolean isModelportfolio();

  String getLetztesUpdate();

  @JsonProperty("gesamtwert")
  double getGesamtwert();

  @JsonProperty("depots")
  FfbDepotliste getDepots();

  static ImmutableMyFfbResponse.Builder builder() {
    return ImmutableMyFfbResponse.builder();
  }
}
