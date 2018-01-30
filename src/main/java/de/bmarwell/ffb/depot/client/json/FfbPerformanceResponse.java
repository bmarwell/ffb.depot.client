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
import de.bmarwell.ffb.depot.client.util.GermanDateToLocalDateDeserializer;
import de.bmarwell.ffb.depot.client.util.GermanNumberToBigDecimalDeserializer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * Das JSON-Response-Objekt von fidelity.de (FFB), welches Performanceinformationen zu allen Depots dieses Logins enthält.
 */
@Value.Immutable
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

}
