package de.bmarwell.ffb.depot.client.json;

import com.google.common.base.MoreObjects;

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
    return Double.parseDouble(dispositionenBetrag.replace(".", "").replace(",", "."));
  }

  public List<FfbDisposition> getDispositionen() {
    return dispositionen;
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
