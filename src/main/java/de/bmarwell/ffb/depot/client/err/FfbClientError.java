/*
 * (c) Copyright 2016 Hbiscus FFB Connector Developers.
 *
 * This file is part of Hbiscus FFB Connector.
 *
 * Hbiscus FFB Connector is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Hbiscus FFB Connector is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hbiscus FFB Connector.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.bmarwell.ffb.depot.client.err;

/**
 * Client error.
 */
public class FfbClientError extends Exception {

  /**
   * Serial.
   */
  private static final long serialVersionUID = 5924224532535147155L;

  public FfbClientError() {
    super();
  }

  public FfbClientError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public FfbClientError(String message, Throwable cause) {
    super(message, cause);
  }

  public FfbClientError(String message) {
    super(message);
  }

  public FfbClientError(Throwable cause) {
    super(cause);
  }

}
