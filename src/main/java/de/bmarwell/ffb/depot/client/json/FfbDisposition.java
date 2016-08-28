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

import javax.annotation.Nullable;

/**
 * Holds information about future transactions.
 */
@Gson.TypeAdapters
@Value.Immutable
public abstract class FfbDisposition implements Comparable<FfbDisposition> {

  /**
   * Returns the name of the depot this transaction belongs to.
   *
   * @return the name of the depot this transaction belongs to.
   */
  @Value.Parameter
  public abstract String getDepot();

  /**
   * Returns the name of the fund this transaction belongs to.
   *
   * @return the name of the fund this transaction belongs to.
   */
  @Value.Parameter
  public abstract String getFondsname();

  /**
   * The Internation Securities Identification Number of the fund which was traded.
   *
   * @return the ISIN as string.
   */
  @Value.Parameter
  public abstract String getIsin();

  /**
   * The german Wertpapierkennnummer.
   *
   * <p>If you prepend this string with three zeroes (padding), than you have the NSIN.</p>
   *
   * @return the WKN as String.
   */
  @Value.Parameter
  public abstract String getWkn();

  /**
   * The name of the Kapitalanlagegesellschaft, the investment trust.
   *
   * @return the name of the investment trust.
   */
  @Value.Parameter
  public abstract String getKagName();

  /**
   * The type of the order.
   *
   * <p>Can be one of the following:
   * <ul><li>erträgnis</li>
   * <li>Kauf Betrag</li></ul>
   * </p>
   *
   * @return the type ot the order as String.
   */
  @Value.Parameter
  public abstract String getAuftragtyp();

  /**
   * The sub category of the order type (see {@link #getAuftragtyp()}).
   *
   * <p>Known values: <ul><li>Kauf</li></ul></p>
   *
   * @return the sub category of the order.
   */
  @Value.Parameter
  public abstract String getTeilauftragtyp();

  @Value.Parameter
  @SerializedName("eingabedatum")
  protected abstract String getEingabedatumAsString();

  /**
   * The date of when the order was placed.
   *
   * @return the date of when the order was placed.
   */
  @Value.Derived
  @SerializedName("eingabedatumAsDate")
  public LocalDate getEingabedatum() {
    return LocalDate.parse(getEingabedatumAsString(), FfbDepotUtils.GERMAN_DATE_FORMAT);
  }

  /**
   * The account where the amount will be withdrawn from, if applicable.
   *
   * <p>Warning! Can be null as seen here: <a
   * href="https://github.com/bmhm/ffb.depot.client/issues/1#issuecomment-241121829">
   * Comment on github issue #1</a>.<br><br>
   * Known values: <ul><li>Referenzkonto</li></ul>
   * </p>
   *
   * @return the type of the account where the amount will be withdrawn from, if applicable.
   */
  @Value.Parameter
  @Nullable
  public abstract String getVerrechnungskonto();

  @Value.Parameter
  @SerializedName("betrag")
  protected abstract String getBetragAsString();

  /**
   * The amount, probably in EUR (€), this order is worth.
   *
   * @return the amount in EUR (probably).
   */
  @Value.Derived
  @SerializedName("betragAsDouble")
  public double getBetrag() {
    return Double.parseDouble(getBetragAsString().replace(".", "").replace(',', '.'));
  }

  /**
   * How many units of the investment funds are traded.
   *
   * @return the number of units traded.
   */
  @Value.Parameter
  @SerializedName("stuecke")
  protected abstract String getStueckeAsString();

  /**
   * How many units of the investment funds are traded.
   *
   * @return the number of units traded.
   */
  @Value.Derived
  @SerializedName("stueckeAsDouble")
  public double getStuecke() {
    return FfbDepotUtils.convertGermanNumberToDouble(getStueckeAsString());
  }

  @Value.Parameter
  @SerializedName("rabatt")
  @Nullable
  protected abstract String getRabattAsString();

  @Value.Derived
  @SerializedName("rabattAsDouble")
  public double getRabatt() {
    return FfbDepotUtils.convertGermanNumberToDouble(getRabattAsString());
  }

  public static FfbDisposition of(String depot, String fondsname, String isin, String wkn, String kagName, String auftragtyp,
      String teilauftragtyp, String eingabedatum, String verrechnungskonto, String betrag, String stuecke, String rabatt) {
    return ImmutableFfbDisposition.of(depot, fondsname, isin, wkn, kagName, auftragtyp, teilauftragtyp, eingabedatum,
        verrechnungskonto, betrag, stuecke, rabatt);
  }

  /**
   * Compares transactions, sorted by depot, isin, auftragstyp, date, etc.
   *
   * <p>A date sorter might be an interesting alternative.</p>
   */
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
