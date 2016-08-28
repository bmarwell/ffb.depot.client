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

  @Value.Parameter
  public abstract String getDepotname();

  @Value.Parameter
  @SerializedName("depotnummer")
  protected abstract String getDepotNummerAsString();

  @Value.Derived
  public FfbDepotNummer getDepotNummer() {
    return FfbDepotNummer.of(getDepotNummerAsString());
  }

  @Value.Parameter
  @SerializedName("bestand")
  protected abstract String getBestandAsString();

  @Value.Derived
  public double getGesamtDepotBestand() {
    return Double.parseDouble(getBestandAsString().replace(".", "").replace(',', '.'));
  }

  @Value.Parameter
  public abstract List<FfbFondsbestand> getFondsbestaende();

  public static FfbDepotInfo of(
      String depotname,
      String depotnummer,
      String bestand,
      Collection<FfbFondsbestand> bestaende) {
    return ImmutableFfbDepotInfo.of(depotname, depotnummer, bestand, ImmutableList.<FfbFondsbestand>copyOf(bestaende));
  }

  public static FfbDepotInfo of(String depotname, FfbDepotNummer depotnummer, double bestand,
      Collection<FfbFondsbestand> bestaende) {
    return FfbDepotInfo.of(
        depotname,
        depotnummer.getDepotNummer(),
        Double.toString(bestand).replace('.', ','),
        bestaende);
  }

  @Override
  public int compareTo(FfbDepotInfo other) {
    return ComparisonChain.start()
        .compare(this.getDepotNummer(), other.getDepotNummer())
        .compare(this.getDepotname(), other.getDepotname())
        .compare(this.getGesamtDepotBestand(), other.getGesamtDepotBestand())
        .result();
  }
}
