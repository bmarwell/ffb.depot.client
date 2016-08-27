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

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

/**
 * Der FFB-Response zur internen Seite &quot;MyFFB.page&quot;.
 *
 * <p>Leider ist die Benennung seitens der FFB nicht sonderlich glücklich, denn dieses Objekt enthält wirklich alle wichtigen
 * Daten des FFB-Kontos.</p>
 */
@Value.Immutable
@Gson.TypeAdapters
public abstract class MyFfbResponse {

  @SerializedName("login")
  @Value.Parameter
  protected abstract String isLoginAsString();

  @Value.Derived
  public boolean isLoggedIn() {
    return Boolean.parseBoolean(isLoginAsString());
  }

  @SerializedName("modelportfolio")
  @Value.Parameter
  public abstract boolean isModelportfolio();

  @Value.Parameter
  public abstract String getLetztesUpdate();

  @SerializedName("gesamtwert")
  @Value.Parameter
  protected abstract String getGesamtwertAsString();

  @Value.Derived
  @SerializedName("gesamtwertAsDouble")
  public double getGesamtwert() {
    return FfbDepotUtils.convertGermanNumberToDouble(getGesamtwertAsString());
  }

  @SerializedName("depots")
  @Value.Parameter
  public abstract FfbDepotliste getDepots();

  public static MyFfbResponse of(String loggedIn, boolean modelportfolio, String letztesUpdate, String gesamtwert,
      FfbDepotliste depots) {
    return ImmutableMyFfbResponse.of(loggedIn, modelportfolio, letztesUpdate, gesamtwert, depots);
  }

  public static MyFfbResponse of(boolean loggedIn, boolean modelportfolio, String letztesUpdate, double gesamtwert,
      FfbDepotliste depots) {
    return ImmutableMyFfbResponse.of(Boolean.toString(loggedIn), modelportfolio, letztesUpdate,
        Double.toString(gesamtwert), depots);
  }
}
