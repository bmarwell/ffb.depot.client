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
import java.util.Comparator;
import org.immutables.value.Value;

/**
 * A single fund representation.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableFfbFondsbestand.class)
public interface FfbFondsbestand extends Comparable<FfbFondsbestand> {

  /**
   * German funds identification number. Wertpapierkennnummer.
   *
   * @return the WKN.
   */
  String getWkn();

  /**
   * International funds number.
   *
   * @return the ISIN as string.
   */
  String getIsin();

  String getFondsname();

  String getFondswaehrung();

  @JsonProperty("bestandStueckzahl")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getBestandStueckzahl();


  @JsonProperty("bestandWertInFondswaehrung")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getBestandWertInFondswaehrung();

  @JsonProperty("bestandWertInEuro")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getBestandWertInEuro();

  @JsonProperty("ruecknahmepreis")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getRuecknahmePreis();

  @JsonProperty("preisDatum")
  @JsonDeserialize(using = GermanDateToLocalDateDeserializer.class)
  LocalDate getPreisDatum();

  @JsonProperty("benchmarkName")
  String getBenchmarkName();

  /**
   * Compares by ISIN and WKN, then the currency and the amount of units, then the worth of the funds, the price date and the
   * benchmark name.
   *
   * <p>Other possible comperators: By unit amount ({@link #getBestandStueckzahl()}) and Worth ({@link #getRuecknahmePreis()}
   * ).
   */
  @Override
  default int compareTo(final FfbFondsbestand other) {
    final Comparator<FfbFondsbestand> comparator = Comparator
        .comparing(FfbFondsbestand::getIsin)
        .thenComparing(FfbFondsbestand::getWkn)
        .thenComparing(FfbFondsbestand::getFondsname)
        .thenComparing(FfbFondsbestand::getFondswaehrung)
        .thenComparing(FfbFondsbestand::getBestandStueckzahl)
        .thenComparing(FfbFondsbestand::getBestandWertInEuro)
        .thenComparing(FfbFondsbestand::getPreisDatum)
        .thenComparing(FfbFondsbestand::getBenchmarkName);

    return comparator.compare(this, other);
  }

}
