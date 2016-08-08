package de.bmarwell.ffb.depot.client.json;

import com.google.common.base.MoreObjects;

import org.threeten.bp.LocalDate;

public class FfbFondsbestand {
  private String wkn;
  private String isin;
  private String fondsname;
  private String fondswaehrung;
  private String bestandStueckzahl;
  private String bestandWertInFondswaehrung;
  private String bestandWertInEuro;
  private String ruecknahmepreis;
  private String preisDatum;
  private String benchmarkName;

  public String getWkn() {
    return wkn;
  }

  public void setWkn(String wkn) {
    this.wkn = wkn;
  }

  public String getIsin() {
    return isin;
  }

  public void setIsin(String isin) {
    this.isin = isin;
  }

  public String getFondsname() {
    return fondsname.trim();
  }

  public void setFondsname(String fondsname) {
    this.fondsname = fondsname;
  }

  public String getFondswaehrung() {
    return fondswaehrung;
  }

  public void setFondswaehrung(String fondswaehrung) {
    this.fondswaehrung = fondswaehrung;
  }

  public double getBestandStueckzahl() {
    return Double.parseDouble(bestandStueckzahl.replace(".", "").replace(",", "."));
  }

  public void setBestandStueckzahl(String bestandStueckzahl) {
    this.bestandStueckzahl = bestandStueckzahl;
  }

  public double getBestandWertInFondswaehrung() {
    return Double.parseDouble(bestandWertInFondswaehrung.replace(".", "").replace(",", "."));
  }

  public void setBestandWertInFondswaehrung(String bestandWertInFondswaehrung) {
    this.bestandWertInFondswaehrung = bestandWertInFondswaehrung;
  }

  public double getBestandWertInEuro() {
    return Double.parseDouble(bestandWertInEuro.replace(".", "").replace(",", "."));
  }

  public void setBestandWertInEuro(String bestandWertInEuro) {
    this.bestandWertInEuro = bestandWertInEuro;
  }

  public String getRuecknahmepreis() {
    return ruecknahmepreis;
  }

  public void setRuecknahmepreis(String ruecknahmepreis) {
    this.ruecknahmepreis = ruecknahmepreis;
  }

  public LocalDate getPreisDatum() {
    return LocalDate.parse(preisDatum, FfbPerformanceResponse.GERMAN_DATE_FORMAT);
  }

  public void setPreisDatum(String preisDatum) {
    this.preisDatum = preisDatum;
  }

  public String getBenchmarkName() {
    return benchmarkName;
  }

  public void setBenchmarkName(String benchmarkName) {
    this.benchmarkName = benchmarkName;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("wkn", wkn)
        .add("isin", isin)
        .add("fondsname", fondsname)
        .add("fondswaehrung", fondswaehrung)
        .add("bestandStueckzahl", bestandStueckzahl)
        .add("bestandWertInFondswaehrung", bestandWertInFondswaehrung)
        .add("bestandWertInEuro", bestandWertInEuro)
        .add("ruecknahmepreis", ruecknahmepreis)
        .add("preisDatum", preisDatum)
        .add("benchmarkName", benchmarkName)
        .toString();
  }
}
