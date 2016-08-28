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

import java.util.ArrayList;

/**
 * A wrapper arround <code>List&lt;FfbDepotInfo&gt;</code> for gson/json marshalling.
 */
public class FfbDepotliste extends ArrayList<FfbDepotInfo> {

  /**
   * Serial.
   */
  private static final long serialVersionUID = -873301810368313616L;

  /**
   * Copies the contents of this list into a new object.
   *
   * <p>This is not a deep copy, only the references are copied to a new list.</p>
   *
   * @return a copy of the contents.
   */
  public FfbDepotliste copy() {
    FfbDepotliste ffbDepotliste = new FfbDepotliste();

    /* iterate over self to get the contents */
    for (FfbDepotInfo info : this) {
      /* The copy contains the same references, i.e. it is not a deep copy. */
      ffbDepotliste.add(info);
    }

    return ffbDepotliste;
  }
}
