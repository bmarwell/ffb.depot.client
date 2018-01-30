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

import de.bmarwell.ffb.depot.client.util.GermanDateToLocalDateDeserializer;
import de.bmarwell.ffb.depot.client.util.GermanNumberToBigDecimalDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.immutables.value.Value;

/**
 * Der FFB-Response zur internen Seite &quot;MyFFB.page&quot;.
 *
 * <p>Leider ist die Benennung seitens der FFB nicht sonderlich glücklich, denn dieses Objekt enthält wirklich alle wichtigen
 * Daten des FFB-Kontos.</p>
 */
@Value.Immutable
@JsonSerialize(as = ImmutableMyFfbResponse.class)
@JsonDeserialize(as = ImmutableMyFfbResponse.class)
public interface MyFfbResponse {

  static ImmutableMyFfbResponse.Builder builder() {
    return ImmutableMyFfbResponse.builder();
  }

  @JsonProperty("login")
  boolean isLoggedIn();

  @JsonProperty("modelportfolio")
  boolean isModelportfolio();

  @JsonDeserialize(using = GermanDateToLocalDateDeserializer.class)
  LocalDate getLetztesUpdate();

  @JsonProperty("gesamtwert")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getGesamtwert();

  @JsonProperty("depots")
  List<FfbDepotInfo> getDepots();
}
