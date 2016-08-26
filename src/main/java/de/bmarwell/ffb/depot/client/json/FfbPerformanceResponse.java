package de.bmarwell.ffb.depot.client.json;

import com.google.common.base.MoreObjects;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Das JSON-Response-Objekt von fidelity.de (FFB), welches Performanceinformationen zu allen Depots dieses Logins enthält.
 */
public class FfbPerformanceResponse {

  private boolean login;
  private String performanceGesamt;
  private String performanceDurchschnitt;
  private String ersterZufluss;
  private String errormessage = "";

  /**
   * German date: <code>dd.MM.YYYY</code>.
   *
   * <p>I think it is kind of stupid of the FFB not to use ISO date and then convert it. This date holds no locale
   * information or whatsoever.<br><br> See also: <a href="https://xkcd.com/1179/">XKCD ISO 8601</a></p>
   */
  public static final DateTimeFormatter GERMAN_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  public boolean isLogin() {
    return login;
  }

  public double getPerformanceGesamt() {
    return Double.parseDouble(performanceGesamt.replace(".", "").replace(',', '.'));
  }

  public double getPerformanceDurchschnitt() {
    return Double.parseDouble(performanceDurchschnitt.replace(".", "").replace(',', '.'));
  }

  public LocalDate getErsterZufluss() {
    return LocalDate.parse(ersterZufluss, GERMAN_DATE_FORMAT);
  }

  public String getErrormessage() {
    return errormessage;
  }

  public void setLogin(boolean login) {
    this.login = login;
  }

  public void setPerformanceGesamt(String performanceGesamt) {
    this.performanceGesamt = performanceGesamt;
  }

  public void setPerformanceDurchschnitt(String performanceDurchschnitt) {
    this.performanceDurchschnitt = performanceDurchschnitt;
  }

  public void setErsterZufluss(String ersterZufluss) {
    this.ersterZufluss = ersterZufluss;
  }

  public void setErrormessage(String errormessage) {
    this.errormessage = errormessage;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("login", login)
        .add("performanceGesamt", performanceGesamt)
        .add("performanceDurchschnitt", performanceDurchschnitt)
        .add("ersterZufluss", ersterZufluss)
        .add("errormessage", errormessage)
        .toString();
  }
}
