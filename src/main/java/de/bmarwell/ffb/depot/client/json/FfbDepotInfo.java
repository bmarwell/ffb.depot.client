package de.bmarwell.ffb.depot.client.json;

import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import com.google.common.base.MoreObjects;

public class FfbDepotInfo {
  private String depotname;
  private String depotnummer;
  private String bestand;

  public String getDepotname() {
    return depotname;
  }

  public void setDepotname(String depotname) {
    this.depotname = depotname;
  }

  public String getDepotnummer() {
    return depotnummer;
  }

  public void setDepotnummer(String depotnummer) {
    this.depotnummer = depotnummer;
  }

  public void setDepotnummer(FfbDepotNummer depotnummer) {
    this.depotnummer = depotnummer.getDepotNummer();
  }

  public double getBestand() {
    return Double.parseDouble(bestand.replace(".", "").replace(",", "."));
  }

  public void setBestand(String bestand) {
    this.bestand = bestand;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("depotname", depotname)
        .add("depotnummer", depotnummer)
        .add("bestand", getBestand())
        .toString();
  }
}
