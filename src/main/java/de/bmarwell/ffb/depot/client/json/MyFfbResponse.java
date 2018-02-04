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
