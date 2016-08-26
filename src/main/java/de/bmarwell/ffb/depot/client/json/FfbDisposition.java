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
