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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * Das JSON-Response-Objekt von fidelity.de (FFB), welches Performanceinformationen zu allen Depots dieses Logins enthält.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableFfbPerformanceResponse.class)
public interface FfbPerformanceResponse {

  @JsonProperty("login")
  boolean isLogin();

  @JsonProperty("performanceGesamt")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getPerformanceGesamt();

  @JsonProperty("performanceDurchschnitt")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getPerformanceDurchschnitt();

  @JsonProperty("ersterZufluss")
  @JsonDeserialize(using = GermanDateToLocalDateDeserializer.class)
  LocalDate getErsterZufluss();

  Optional<String> getErrormessage();

  @Value.Check
  default FfbPerformanceResponse normalize() {
    if (getErrormessage().orElse("NONEMPTY").isEmpty()) {
      return ImmutableFfbPerformanceResponse.copyOf(this)
          .withErrormessage(Optional.empty());
    }

    return this;
  }

}
