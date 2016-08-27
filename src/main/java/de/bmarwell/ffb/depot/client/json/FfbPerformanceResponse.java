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
import org.threeten.bp.LocalDate;

/**
 * Das JSON-Response-Objekt von fidelity.de (FFB), welches Performanceinformationen zu allen Depots dieses Logins enthält.
 */
@Value.Immutable
@Gson.TypeAdapters
public abstract class FfbPerformanceResponse {

  @SerializedName("login")
  protected abstract String isLoginAsString();

  public boolean isLogin() {
    return Boolean.parseBoolean(isLoginAsString());
  }

  @SerializedName("performanceGesamt")
  protected abstract String getPerformanceGesamtAsString();

  public double getPerformanceGesamt() {
    return FfbDepotUtils.convertGermanNumberToDouble(getPerformanceGesamtAsString());
  }

  @SerializedName("performanceDurchschnitt")
  protected abstract String getPerformanceDurchschnittAsString();

  public double getPerformanceDurchschnitt() {
    return FfbDepotUtils.convertGermanNumberToDouble(getPerformanceDurchschnittAsString());
  }

  @SerializedName("ersterZufluss")
  protected abstract String getErsterZuflussAsString();

  public LocalDate getErsterZufluss() {
    return FfbDepotUtils.convertGermanDateToLocalDate(getErsterZuflussAsString());
  }

  public abstract String getErrormessage();

}
