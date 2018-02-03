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

import de.bmarwell.ffb.depot.client.json.ImmutableFfbDisposition.Builder;
import de.bmarwell.ffb.depot.client.util.GermanDateToLocalDateDeserializer;
import de.bmarwell.ffb.depot.client.util.GermanNumberToBigDecimalDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * Holds information about future transactions.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableFfbDisposition.class)
public interface FfbDisposition extends Comparable<FfbDisposition> {

  static Builder builder() {
    return ImmutableFfbDisposition.builder();
  }

  /**
   * Returns the name of the depot this transaction belongs to.
   *
   * @return the name of the depot this transaction belongs to.
   */
  @JsonProperty("depot")
  String getDepot();

  /**
   * Returns the name of the fund this transaction belongs to.
   *
   * @return the name of the fund this transaction belongs to.
   */
  @JsonProperty("fondsname")
  String getFondsname();

  /**
   * The Internation Securities Identification Number of the fund which was traded.
   *
   * @return the ISIN as string.
   */
  @JsonProperty("isin")
  String getIsin();

  /**
   * The german Wertpapierkennnummer.
   *
   * <p>If you prepend this string with three zeroes (padding), than you have the NSIN.</p>
   *
   * @return the WKN as String.
   */
  @JsonProperty("wkn")
  String getWkn();

  /**
   * The name of the Kapitalanlagegesellschaft, the investment trust.
   *
   * @return the name of the investment trust.
   */
  @JsonProperty("kagName")
  String getKagName();

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
  @JsonProperty("auftragtyp")
  String getAuftragtyp();

  /**
   * The sub category of the order type (see {@link #getAuftragtyp()}).
   *
   * <p>Known values: <ul><li>Kauf</li></ul></p>
   *
   * @return the sub category of the order.
   */
  @JsonProperty("teilauftragtyp")
  String getTeilauftragtyp();

  /**
   * Date of entry as string (when the order was placed).
   *
   * @return the date of entry as String
   */
  @JsonProperty("eingabedatum")
  @JsonDeserialize(using = GermanDateToLocalDateDeserializer.class)
  LocalDate getEingabedatum();

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
  @JsonProperty("verrechnungskonto")
  String getVerrechnungskonto();

  /**
   * The worth of the order as string, german format (decimal comma, thousands dots).
   *
   * @return the betrag as string.
   */
  @JsonProperty("betrag")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getBetrag();

  /**
   * How many units of the investment funds are traded.
   *
   * @return the number of units traded.
   */
  @JsonProperty("stuecke")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  BigDecimal getStuecke();

  /**
   * The rabatt (discount) in german format (decimal comma).
   *
   * @return the rabatt as string.
   */
  @JsonProperty("rabatt")
  @JsonDeserialize(using = GermanNumberToBigDecimalDeserializer.class)
  Optional<BigDecimal> getRabatt();

  /**
   * Compares transactions, sorted by depot, isin, auftragstyp, date, etc.
   *
   * <p>A date sorter might be an interesting alternative.</p>
   */
  @Override
  default int compareTo(final FfbDisposition other) {
    final Comparator<FfbDisposition> comparator = Comparator
        .comparing(FfbDisposition::getDepot)
        .thenComparing(FfbDisposition::getIsin)
        .thenComparing(FfbDisposition::getAuftragtyp)
        .thenComparing(FfbDisposition::getEingabedatum)
        .thenComparing(FfbDisposition::getBetrag)
        .thenComparing(FfbDisposition::getStuecke);

    return comparator.compare(this, other);
  }


}
