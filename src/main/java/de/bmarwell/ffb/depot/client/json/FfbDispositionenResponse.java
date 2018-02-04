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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * The response object for the Dispositionen page (order history).
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableFfbDispositionenResponse.class)
public interface FfbDispositionenResponse {

  /**
   * Boolean as string, if user is logged in.
   *
   * @return {@code true}, if logged in.
   */
  @JsonProperty("login")
  boolean isLogin();

  @JsonProperty("dispositionenAnzahl")
  int getDispositionenAnzahl();

  @JsonProperty("dispositionenBetrag")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getDispositionenBetrag();

  List<FfbDisposition> getDispositionen();

  /**
   * If non- empty, error.
   *
   * @return error message.
   */
  Optional<String> getErrormessage();

  @Value.Check
  default FfbDispositionenResponse normalize() {
    if (getErrormessage().orElse("NONEMPTY").isEmpty()) {
      return ImmutableFfbDispositionenResponse.copyOf(this)
          .withErrormessage(Optional.empty());
    }

    return this;
  }

}
