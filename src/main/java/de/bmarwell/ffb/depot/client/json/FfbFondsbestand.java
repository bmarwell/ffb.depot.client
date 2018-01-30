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
  default int compareTo(FfbFondsbestand other) {
    Comparator comparator = Comparator
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
