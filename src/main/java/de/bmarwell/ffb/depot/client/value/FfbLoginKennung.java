package de.bmarwell.ffb.depot.client.value;

public class FfbLoginKennung {
  private String login;

  private FfbLoginKennung(String loginkennung) {
    this.login = loginkennung;
  }


  public static FfbLoginKennung of(String loginkennung) {
    return new FfbLoginKennung(loginkennung);
  }

  public String getLoginKennung() {
    return new String(login);
  }
}
