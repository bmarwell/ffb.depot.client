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

@Gson.TypeAdapters
@Value.Immutable
public abstract class FfbDisposition implements Comparable<FfbDisposition> {

  @Value.Parameter
  public abstract String getDepot();

  @Value.Parameter
  public abstract String getFondsname();

  @Value.Parameter
  public abstract String getIsin();

  @Value.Parameter
  public abstract String getWkn();

  @Value.Parameter
  public abstract String getKagName();

  @Value.Parameter
  public abstract String getAuftragtyp();

  @Value.Parameter
  public abstract String getTeilauftragtyp();

  @Value.Parameter
  @SerializedName("eingabedatum")
  protected abstract String getEingabedatumAsString();

  public LocalDate getEingabedatum() {
    return LocalDate.parse(getEingabedatumAsString(), FfbDepotUtils.GERMAN_DATE_FORMAT);
  }

  @Value.Parameter
  public abstract String getVerrechnungskonto();

  @Value.Parameter
  @SerializedName("betrag")
  protected abstract String getBetragAsString();

  public double getBetrag() {
    return Double.parseDouble(getBetragAsString().replace(".", "").replace(',', '.'));
  }

  @Value.Parameter
  @SerializedName("stuecke")
  protected abstract String getStueckeAsString();

  public double getStuecke() {
    return Double.parseDouble(getStueckeAsString().replace(".", "").replace(',', '.'));
  }

  public static FfbDisposition of(String depot, String fondsname, String isin, String wkn, String kagName, String auftragtyp,
      String teilauftragtyp, String eingabedatum, String verrechnungskonto, String betrag, String stuecke) {
    return ImmutableFfbDisposition.of(depot, fondsname, isin, wkn, kagName, auftragtyp, teilauftragtyp, eingabedatum,
        verrechnungskonto, betrag, stuecke);
  }

  @Override
  public int compareTo(FfbDisposition other) {
    return ComparisonChain.start()
        .compare(this.getDepot(), other.getDepot())
        .compare(this.getIsin(), other.getIsin())
        .compare(this.getAuftragtyp(), other.getAuftragtyp())
        .compare(this.getEingabedatum(), other.getEingabedatum())
        .compare(this.getBetrag(), other.getBetrag())
        .compare(this.getStuecke(), other.getStuecke())
        .result();
  }

}
