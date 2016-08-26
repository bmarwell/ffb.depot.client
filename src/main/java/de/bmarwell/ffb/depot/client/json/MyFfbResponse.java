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
    return Double.parseDouble(gesamtwert.replace(".", "").replace(',', '.'));
  }

  public void setGesamtwert(String gesamtwert) {
    this.gesamtwert = gesamtwert;
  }

  public FfbDepotliste getDepots() {
    return depots.copy();
  }

  public void setDepots(FfbDepotliste depots) {
    this.depots = depots.copy();
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
