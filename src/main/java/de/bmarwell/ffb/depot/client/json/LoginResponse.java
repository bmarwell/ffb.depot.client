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

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

/**
 * Information which is sent by the server on successful login.
 */
@Value.Immutable
@Gson.TypeAdapters
public abstract class LoginResponse {

  @SerializedName("loggedIn")
  protected abstract String isLoggedInAsString();

  public boolean isLoggedIn() {
    return Boolean.parseBoolean(isLoggedInAsString());
  }

  /**
   * The name of the user.
   *
   * @return the username.
   */
  @SerializedName("username")
  public abstract String getUsername();

  /**
   * The customers first name.
   *
   * @return the first name.
   */
  public abstract String getFirstname();

  /**
   * The customers last name.
   *
   * @return the last name.
   */
  public abstract String getLastname();

  /**
   * The type of the user.
   *
   * <p>Known values:<ul><li>Customer</li></ul></p>
   *
   * @return the user type.
   */
  public abstract String getUsertype();

  @SerializedName("ZustimmungNutzungsbedingungenFFS")
  protected abstract String getAgbAgreedAsString();

  /**
   * True if user agreed to AGB (terms and conditions)
   *
   * @return true if he agreed.
   */
  public boolean getAgbAgreed() {
    return Boolean.parseBoolean(getAgbAgreedAsString());
  }

  /**
   * Error message set by FFB.
   * 
   * @return the error message.
   */
  public abstract String getErrormessage();

}
