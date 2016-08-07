package de.bmarwell.ffb.depot.client.json;

import com.google.common.base.MoreObjects;

/**
 * Der FFB-Response zur internen Seite &quot;MyFFB.page&quot;.
 *
 * <p>Leider ist die Benennung seitens der FFB nicht sonderlich glücklich, denn dieses Objekt enthält wirklich alle wichtigen
 * Daten des FFB-Kontos.</p>
 */
public class MyFfbResponse {
  private boolean login;
  private boolean modelportfolio;
  private String letztesUpdate;
  private String gesamtwert;

  private FfbDepotliste depots = new FfbDepotliste();

  public boolean isLogin() {
    return login;
  }

  public void setLogin(boolean login) {
    this.login = login;
  }

  public boolean isModelportfolio() {
    return modelportfolio;
  }

  public void setModelportfolio(boolean modelportfolio) {
    this.modelportfolio = modelportfolio;
  }

  public String getLetztesUpdate() {
    return letztesUpdate;
  }

  public void setLetztesUpdate(String letztesUpdate) {
    this.letztesUpdate = letztesUpdate;
  }

  public double getGesamtwert() {
    return Double.parseDouble(gesamtwert.replace(".", "").replace(",", "."));
  }

  public void setGesamtwert(String gesamtwert) {
    this.gesamtwert = gesamtwert;
  }

  public FfbDepotliste getDepots() {
    return depots;
  }

  public void setDepots(FfbDepotliste depots) {
    this.depots = depots;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("login", login)
        .add("modelportfolio", modelportfolio)
        .add("letztesUpdate", letztesUpdate)
        .add("gesamtwert", getGesamtwert())
        .add("depots", depots)
        .toString();
  }
}
