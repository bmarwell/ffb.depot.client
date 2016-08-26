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

package de.bmarwell.ffb.depot.client.value;

import de.bmarwell.ffb.depot.client.FfbMobileClient;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Dieses Objekt hält eine FFB-Depotnummer.
 *
 * <p>Ein gesondertes Objekt gegenüber der {@link FfbLoginKennung} ist notwendig, da die Loginkennung geändert werden kann.
 * Die Depotnummer enthält oftmals nicht das <i>-01</i>-Suffix, wird von der FFB fest vergeben und kann nicht geändert
 * werden.</p>
 */
public class FfbDepotNummer {

  private String depotnummer;

  private FfbDepotNummer(String depotnummer) {
    this.depotnummer = depotnummer;
  }

  /**
   * Erstellt eine Depotnummer als Immutable Objekt, die dem Konstruktor {@link FfbMobileClient} übergeben werden kann.
   *
   * @param depotnummer
   *          Die Depotnummer als String.
   *
   *          <p><b>Hinweis:</b> Sie muss nicht gleich mit dem Login sein. Der Standard-Login hat noch ein <i>-01</i> als
   *          Suffix, oder wurde vom Benutzer in etwas ganz anderes geändert. Die Depotnummer hingegen wurde von der FFB
   *          vergeben und kann nicht geändert werden.</p>
   * @return ein Depotnummer-Objekt, immutable.
   */
  public static FfbDepotNummer of(String depotnummer) {
    return new FfbDepotNummer(depotnummer);
  }

  public String getDepotNummer() {
    return this.depotnummer;
  }

  public static FfbDepotNummer empty() {
    return of("");
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("depotnummer", getDepotNummer())
        .toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (!this.getClass().equals(obj.getClass())) {
      return false;
    }

    FfbDepotNummer other = (FfbDepotNummer) obj;

    return this.getDepotNummer().equals(other.getDepotNummer());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDepotNummer());
  }
}
