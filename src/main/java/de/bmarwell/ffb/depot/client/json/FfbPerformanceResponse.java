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

import de.bmarwell.ffb.depot.client.FfbDepotUtils;

import com.google.common.base.MoreObjects;

import org.threeten.bp.LocalDate;

/**
 * Das JSON-Response-Objekt von fidelity.de (FFB), welches Performanceinformationen zu allen Depots dieses Logins enthält.
 */
public class FfbPerformanceResponse {

  private boolean login;
  private String performanceGesamt;
  private String performanceDurchschnitt;
  private String ersterZufluss;
  private String errormessage = "";

  public boolean isLogin() {
    return login;
  }

  public double getPerformanceGesamt() {
    return Double.parseDouble(performanceGesamt.replace(".", "").replace(',', '.'));
  }

  public double getPerformanceDurchschnitt() {
    return Double.parseDouble(performanceDurchschnitt.replace(".", "").replace(',', '.'));
  }

  public LocalDate getErsterZufluss() {
    return LocalDate.parse(ersterZufluss, FfbDepotUtils.GERMAN_DATE_FORMAT);
  }

  public String getErrormessage() {
    return errormessage;
  }

  public void setLogin(boolean login) {
    this.login = login;
  }

  public void setPerformanceGesamt(String performanceGesamt) {
    this.performanceGesamt = performanceGesamt;
  }

  public void setPerformanceDurchschnitt(String performanceDurchschnitt) {
    this.performanceDurchschnitt = performanceDurchschnitt;
  }

  public void setErsterZufluss(String ersterZufluss) {
    this.ersterZufluss = ersterZufluss;
  }

  public void setErrormessage(String errormessage) {
    this.errormessage = errormessage;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("login", login)
        .add("performanceGesamt", performanceGesamt)
        .add("performanceDurchschnitt", performanceDurchschnitt)
        .add("ersterZufluss", ersterZufluss)
        .add("errormessage", errormessage)
        .toString();
  }
}
