package de.bmarwell.ffb.depot.client.json;

import java.util.ArrayList;

public class FfbDepotliste extends ArrayList<FfbDepotInfo> {

  /**
   * Serial.
   */
  private static final long serialVersionUID = -873301810368313616L;

  public FfbDepotliste copy() {
    FfbDepotliste ffbDepotliste = new FfbDepotliste();

    for (FfbDepotInfo info : this) {
      ffbDepotliste.add(info);
    }

    return ffbDepotliste;
  }
}
