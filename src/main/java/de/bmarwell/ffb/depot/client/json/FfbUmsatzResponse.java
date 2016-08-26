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
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class FfbUmsatzResponse {
  private boolean login;
  private int dispositionenAnzahl;
  private String dispositionenBetrag;
  private List<FfbDisposition> dispositionen = new ArrayList<>();
  private String errormessage;

  public boolean isLogin() {
    return login;
  }

  public int getDispositionenAnzahl() {
    return dispositionenAnzahl;
  }

  public double getDispositionenBetrag() {
    return Double.parseDouble(dispositionenBetrag.replace(".", "").replace(',', '.'));
  }

  public List<FfbDisposition> getDispositionen() {
    return ImmutableList.<FfbDisposition>copyOf(dispositionen);
  }

  public String getErrormessage() {
    return errormessage;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("login", login)
        .add("dispositionenAnzahl", getDispositionenAnzahl())
        .add("dispositionenBetrag", getDispositionenBetrag())
        .add("dispositionen", getDispositionen())
        .add("errormessage", getErrormessage())
        .toString();
  }

}
