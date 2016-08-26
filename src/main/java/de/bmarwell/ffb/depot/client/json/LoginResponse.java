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

public class LoginResponse {

  private boolean loggedIn;
  private String username;
  private String firstname;
  private String lastname;
  private String usertype;

  @SerializedName("ZustimmungNutzungsbedingungenFFS")
  private String agbAgreed;
  private String errormessage;

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getUsertype() {
    return usertype;
  }

  public void setUsertype(String usertype) {
    this.usertype = usertype;
  }

  public String getAgbAgreed() {
    return agbAgreed;
  }

  public void setAgbAgreed(String agbAgreed) {
    this.agbAgreed = agbAgreed;
  }

  public String getErrormessage() {
    return errormessage;
  }

  public void setErrormessage(String errormessage) {
    this.errormessage = errormessage;
  }

  @Override
  public String toString() {
    return "LoginResponse [loggedIn=" + loggedIn + ", username=" + username + ", firstname=" + firstname + ", lastname="
        + lastname + ", usertype=" + usertype + ", agbAgreed=" + agbAgreed + ", errormessage=" + errormessage + "]";
  }

}
