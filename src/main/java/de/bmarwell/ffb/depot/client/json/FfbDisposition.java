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

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;

import org.threeten.bp.LocalDate;

public class FfbDisposition implements Comparable<FfbDisposition> {
  private String depot;
  private String fondsname;
  private String isin;
  private String wkn;
  private String kagName;
  private String auftragtyp;
  private String teilauftragtyp;
  private String eingabedatum;
  private String verrechnungskonto;
  private String betrag;
  private String stuecke;

  /**
   * empty constructor for json/gson.
   */
  public FfbDisposition() {
    // empty constructor for json/gson
  }

  public FfbDisposition(String depot, String fondsname, String isin, String wkn, String kagName, String auftragtyp,
      String teilauftragtyp, String eingabedatum, String verrechnungskonto, String betrag, String stuecke) {
    this.depot = depot;
    this.fondsname = fondsname;
    this.isin = isin;
    this.wkn = wkn;
    this.kagName = kagName;
    this.auftragtyp = auftragtyp;
    this.teilauftragtyp = teilauftragtyp;
    this.eingabedatum = eingabedatum;
    this.verrechnungskonto = verrechnungskonto;
    this.betrag = betrag;
    this.stuecke = stuecke;
  }

  public String getDepot() {
    return depot;
  }

  public String getFondsname() {
    return fondsname;
  }

  public String getIsin() {
    return isin;
  }

  public String getWkn() {
    return wkn;
  }

  public String getKagName() {
    return kagName;
  }

  public String getAuftragtyp() {
    return auftragtyp;
  }

  public String getTeilauftragtyp() {
    return teilauftragtyp;
  }

  public LocalDate getEingabedatum() {
    return LocalDate.parse(eingabedatum, FfbPerformanceResponse.GERMAN_DATE_FORMAT);
  }

  public String getVerrechnungskonto() {
    return verrechnungskonto;
  }

  public double getBetrag() {
    return Double.parseDouble(betrag.replace(".", "").replace(',', '.'));
  }

  public double getStuecke() {
    return Double.parseDouble(stuecke.replace(".", "").replace(',', '.'));
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("depot", getDepot())
        .add("fondsnamme", getFondsname())
        .add("isin", getIsin())
        .add("wkn", getWkn())
        .add("kagName", getKagName())
        .add("auftragtyp", getAuftragtyp())
        .add("teilauftragtyp", getTeilauftragtyp())
        .add("eingabedatum", getEingabedatum())
        .add("verrechnungskonto", getVerrechnungskonto())
        .add("betrag", getBetrag())
        .add("stuecke", getStuecke())
        .toString();
  }

}
