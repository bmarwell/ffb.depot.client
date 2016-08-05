package de.bmarwell.ffb.depot.client;

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
