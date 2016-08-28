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

import de.bmarwell.ffb.depot.client.FfbDepotUtils;

import com.google.common.collect.ComparisonChain;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.threeten.bp.LocalDate;

/**
 * A single fund representation.
 */
@Value.Immutable
@Gson.TypeAdapters
public abstract class FfbFondsbestand implements Comparable<FfbFondsbestand> {

  /**
   * German funds identification number. Wertpapierkennnummer.
   *
   * @return the WKN.
   */
  @Value.Parameter
  public abstract String getWkn();

  /**
   * International funds number.
   *
   * @return the ISIN as string.
   */
  @Value.Parameter
  public abstract String getIsin();

  @Value.Parameter
  public abstract String getFondsname();

  @Value.Parameter
  public abstract String getFondswaehrung();

  @Value.Parameter
  @SerializedName("bestandStueckzahl")
  protected abstract String getBestandStueckzahlAsString();

  @Value.Derived
  @SerializedName("bestandStueckzahlAsDouble")
  public double getBestandStueckzahl() {
    return FfbDepotUtils.convertGermanNumberToDouble(getBestandStueckzahlAsString());
  }

  @Value.Parameter
  @SerializedName("bestandWertInFondswaehrung")
  public abstract String getBestandWertInFondswaehrungAsString();

  @Value.Derived
  @SerializedName("bestandWertInFondswaehrungAsDouble")
  public double getBestandWertInFondswaehrung() {
    return FfbDepotUtils.convertGermanNumberToDouble(getBestandWertInFondswaehrungAsString());
  }

  @Value.Parameter
  @SerializedName("bestandWertInEuro")
  protected abstract String getBestandWertInEuroAsString();

  @Value.Derived
  @SerializedName("bestandWertInEuroAsDouble")
  public double getBestandWertInEuro() {
    return FfbDepotUtils.convertGermanNumberToDouble(getBestandWertInEuroAsString());
  }

  @Value.Parameter
  @SerializedName("ruecknahmepreis")
  protected abstract String getRuecknahmePreisAsString();

  @Value.Derived
  @SerializedName("ruecknahmepreisAsDouble")
  public double getRuecknahmePreis() {
    return FfbDepotUtils.convertGermanNumberToDouble(getRuecknahmePreisAsString());
  }

  @Value.Parameter
  @SerializedName("preisDatum")
  protected abstract String getPreisDatumAsString();

  @Value.Derived
  @SerializedName("preisDatumAsDate")
  public LocalDate getPreisdatum() {
    return FfbDepotUtils.convertGermanDateToLocalDate(getPreisDatumAsString());
  }

  @Value.Parameter
  @SerializedName("benchmarkName")
  public abstract String getBenchmarkName();

  /**
   * Compares by ISIN and WKN, then the currency and the amount of units, then the worth of the funds, the price date and the
   * benchmark name.
   *
   * <p>Other possible comperators: By unit amount ({@link #getBestandStueckzahl()}) and Worth ({@link #getRuecknahmePreis()}
   * ).
   */
  @Override
  public int compareTo(FfbFondsbestand other) {
    return ComparisonChain.start()
        .compare(this.getIsin(), other.getIsin())
        .compare(this.getWkn(), other.getWkn())
        .compare(this.getFondsname(), other.getFondsname())
        .compare(this.getFondswaehrung(), other.getFondswaehrung())
        .compare(this.getBestandStueckzahl(), other.getBestandStueckzahl())
        .compare(this.getBestandWertInEuro(), other.getBestandWertInEuro())
        .compare(this.getPreisdatum(), other.getPreisdatum())
        .compare(this.getBenchmarkName(), other.getBenchmarkName())
        .result();
  }

}
