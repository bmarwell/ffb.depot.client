package de.bmarwell.ffb.depot.client.value;

public class FfbDepotNummer {

  private String depotnummer;

  private FfbDepotNummer(String depotnummer) {
    this.depotnummer = depotnummer;
  }

  public static FfbDepotNummer of(String depotnummer) {
    return new FfbDepotNummer(depotnummer);
  }

  public String getDepotNummer() {
    return this.depotnummer;
  }

  public static FfbDepotNummer empty() {
    return of("");
  }
}
