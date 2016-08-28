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

import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.Collection;
import java.util.List;

/**
 * Information about a depot. You can have multiple Depots in your login view, thus this class should be comparable/sortable.
 */
@Gson.TypeAdapters
@Value.Immutable
public abstract class FfbDepotInfo implements Comparable<FfbDepotInfo> {

  /**
   * A name of th depot. Actually, it is much more like a category ("Standarddepot", "VL-Depot", etc.).
   *
   * @return the Depotname as String.
   */
  @Value.Parameter
  public abstract String getDepotname();

  @Value.Parameter
  @SerializedName("depotnummer")
  protected abstract String getDepotNummerAsString();

  /**
   * The depotnumber as value class.
   * 
   * @return the depotnumber as value class.
   */
  @Value.Derived
  public FfbDepotNummer getDepotNummer() {
    return FfbDepotNummer.of(getDepotNummerAsString());
  }

  @Value.Parameter
  @SerializedName("bestand")
  protected abstract String getBestandAsString();

  /**
   * The actual worth of this depot in EUR.
   *
   * @return the worth as double.
   */
  @Value.Derived
  public double getGesamtDepotBestand() {
    return Double.parseDouble(getBestandAsString().replace(".", "").replace(',', '.'));
  }

  /**
   * Each fund is represendet by {@link FfbFondsbestand}.
   *
   * @return a list of funds.
   */
  @Value.Parameter
  public abstract List<FfbFondsbestand> getFondsbestaende();

  public static FfbDepotInfo of(
      String depotname,
      String depotnummer,
      String bestand,
      Collection<FfbFondsbestand> bestaende) {
    return ImmutableFfbDepotInfo.of(depotname, depotnummer, bestand, ImmutableList.<FfbFondsbestand>copyOf(bestaende));
  }

  /**
   * Constructor with correct data types.
   *
   * @param depotname
   *          the name of the depot.
   * @param depotnummer
   *          the number of the depot.
   * @param bestand
   *          the amount (probably in EUR) contained in this depot.
   * @param bestaende
   *          a list of {@link FfbFondsbestand}, holdings.
   * @return a {@link FfbDepotInfo} instance.
   */
  public static FfbDepotInfo of(String depotname, FfbDepotNummer depotnummer, double bestand,
      Collection<FfbFondsbestand> bestaende) {
    return FfbDepotInfo.of(
        depotname,
        depotnummer.getDepotNummer(),
        Double.toString(bestand).replace('.', ','),
        bestaende);
  }

  /**
   * Compare to other by {@link #getDepotNummer()}, {@link #getDepotname()} and {@link #getGesamtDepotBestand()}.
   */
  @Override
  public int compareTo(FfbDepotInfo other) {
    return ComparisonChain.start()
        .compare(this.getDepotNummer(), other.getDepotNummer())
        .compare(this.getDepotname(), other.getDepotname())
        .compare(this.getGesamtDepotBestand(), other.getGesamtDepotBestand())
        .result();
  }
}
