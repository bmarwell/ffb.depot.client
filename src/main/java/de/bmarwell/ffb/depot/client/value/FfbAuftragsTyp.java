package de.bmarwell.ffb.depot.client.value;

public enum FfbAuftragsTyp {
  /**
   * Typ: Alle.
   */
  ALLE("Alle"),
  /**
   * Typ: Verkauf wg. Depotführungsentgelte.
   */
  ENTGELTVERKAUF("Entgeltverkauf"), ERTRAG("Ertrag"), KAUF("Kauf"), TAUSCH("Tausch"), UEBERTRAG("Übertrag"),
  /**
   * Typ: Verkauf.
   */
  VERKAUF("");

  /**
   * Name des Auftragstyps.
   */
  private final String auftragsTypName;

  FfbAuftragsTyp(final String auftragsName) {
    this.auftragsTypName = auftragsName;
  }

  /**
   * Gibt den lesbaren Namen des Auftragstyps aus.
   *
   * @return der Name des Auftragstyps.
   */
  public String getAuftragsTypName() {
    return auftragsTypName;
  }
}
