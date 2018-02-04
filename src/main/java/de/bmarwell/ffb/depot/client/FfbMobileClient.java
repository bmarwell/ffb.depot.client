/*
 *  Copyright 2018 The ffb.depot.client contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.bmarwell.ffb.depot.client;

import static java.util.Objects.requireNonNull;

import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbDispositionenResponse;
import de.bmarwell.ffb.depot.client.json.FfbPerformanceResponse;
import de.bmarwell.ffb.depot.client.json.FfbUmsatzResponse;
import de.bmarwell.ffb.depot.client.json.LoginResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.util.WebClientHelper;
import de.bmarwell.ffb.depot.client.value.FfbAuftragsTyp;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import java.time.LocalDate;
import java.util.Map;
import java.util.StringJoiner;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class of the mobile client. It stores the current connection status (logged in etc.) and provides access methods for various account
 * information.
 */
public class FfbMobileClient {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(FfbMobileClient.class);

  /**
   * Helper to build requests.
   */
  private final WebClientHelper webclienthelper;

  /**
   * Saved PIN for use in {@link #logon()}-method.
   */
  private FfbPin pin = FfbPin.of("");

  /**
   * User for use in {@link #logon()}-method.
   */
  private FfbLoginKennung user = FfbLoginKennung.of("");

  /**
   * Konstruktor for tests and internal uses only. PLease use {@link #FfbMobileClient(FfbLoginKennung, FfbPin)} instead.
   *
   * <p>Wird der Client wie hier ohne User und Pin erstellt, kann gar nichts klappen.</p>
   *
   * @param config
   *     Die Konfiguration, die für diese Client-Instanz genutzt werden soll.
   */
  public FfbMobileClient(final FfbClientConfiguration config) {
    this.webclienthelper = new WebClientHelper(config.getBaseUrl(), config.getUserAgent());
  }

  /**
   * Erstellt ein Retriever, der von der FFB über die Mobile-Schnittstelle Daten empfängt.
   *
   * @param user Der Login. Meistens die Depotnummer. Bei mehreren Depots mit selbem Login einfach ebenfalls das Login.
   * @param pin das Passwort fürs Login.
   */
  public FfbMobileClient(final FfbLoginKennung user, final FfbPin pin) {
    this(user, pin, new FfbDefaultConfig());
  }

  /**
   * Erstellt ein Retriever, der von der FFB über die Mobile-Schnittstelle Daten empfängt.
   *
   * @param user
   *     Der Login. Meistens die Depotnummer. Bei mehreren Depots mit selbem Login einfach ebenfalls das Login.
   * @param pin
   *     das Passwort fürs Login.
   * @param config
   *     the configuration to use
   */
  public FfbMobileClient(final FfbLoginKennung user, final FfbPin pin, final FfbClientConfiguration config) {
    this(config);
    this.user = user;
    this.pin = pin;
  }

  /**
   * Diese Methode ermittelt die aktuellen Depotbestände und weitere Informationen aus der Gesamtheit aller Depots.
   *
   * <p><b>Hinweis:</b> Zuvor muss ein {@link #logon()} aufgerufen worden sein, sonst gibt es eine {@link IllegalStateException}.
   *
   * @return die Depotinfo.
   * @throws FfbClientError Error while getting account data.
   */
  public MyFfbResponse fetchAccountData() throws FfbClientError {
    final Invocation target = webclienthelper.accountData();
    final Response response = target.invoke();

    final MyFfbResponse bestandsResponse = response.readEntity(MyFfbResponse.class);

    LOG.debug("BestandsResponse: [{}].", bestandsResponse);

    return bestandsResponse;
  }

  /**
   * Login über Cookies.
   *
   * @throws FfbClientError Error logging in. Wrong login data?
   */
  public void logon() throws FfbClientError {
    webclienthelper.login(user, pin);
  }

  /**
   * Gibt die Performance für alle Depots dieses Logins aus.
   *
   * @return ein {@link FfbPerformanceResponse} mit einigen Performance-Infos.
   * @throws FfbClientError Falls es zuvor keinen (derzeit noch) gültigen Login gab.
   * @throws IllegalStateException if not logged in.
   */
  public FfbPerformanceResponse getPerformance() throws FfbClientError {
    webclienthelper.checkLoggedIn();

    final Response response = webclienthelper.performance().invoke();
    final FfbPerformanceResponse performanceResponse = response.readEntity(FfbPerformanceResponse.class);

    LOG.debug("Performance: [{}].", performanceResponse);

    return performanceResponse;
  }

  /**
   * Response with pending transactions.
   *
   * @return a pending transactions object.
   * @throws FfbClientError login error etc.
   * @throws IllegalStateException if not logged in.
   */
  public FfbDispositionenResponse getDispositionen() throws FfbClientError {
    webclienthelper.checkLoggedIn();

    final Response response = webclienthelper.dispositionen().invoke();
    final FfbDispositionenResponse dispositionenResponse = response.readEntity(FfbDispositionenResponse.class);

    LOG.debug("Dispositionen: [{}].", dispositionenResponse);

    return dispositionenResponse;
  }

  /**
   * @param auftragsTyp
   *     der gewünschte Auftragstyp.
   * @param from
   *     beginn-Zeitraum.
   * @param until
   *     Ende-Zeitraum
   * @return ein Umsatz-Response.
   * @throws IllegalArgumentException
   *     falls der Zeitraum FROM zu lange her ist.
   */
  public FfbUmsatzResponse getUmsaetze(
      final FfbAuftragsTyp auftragsTyp, final LocalDate from, final LocalDate until) {
    webclienthelper.checkLoggedIn();

    requireNonNull(auftragsTyp, "auftragsTyp");
    requireNonNull(auftragsTyp, "from");
    requireNonNull(auftragsTyp, "until");

    /* Make sure, the from date is not older than the other one */
    if (LocalDate.now().minusMonths(5).minusDays(15).isAfter(from)) {
      throw new IllegalArgumentException("Period may not exceed 5 Months, 15 Days ago from now.");
    }

    final Response response = webclienthelper.umsaetze(auftragsTyp, from, until).invoke();
    final FfbUmsatzResponse umsatzResponse = response.readEntity(FfbUmsatzResponse.class);

    LOG.debug("Response Umsätze für Datumsbereich [{}]-[{}] => [{}].", from, until, umsatzResponse);

    return umsatzResponse;
  }

  public boolean logout() throws FfbClientError {
    webclienthelper.checkLoggedIn();

    return webclienthelper.logout();
  }

  public boolean isLoggedIn() {
    return webclienthelper.isLoggedIn();
  }

  public Map<String, NewCookie> getCookies() {
    return webclienthelper.getCookies();
  }


  public LoginResponse loginInformation() {
    return webclienthelper.loginInformation();
  }

  /**
   * Returns the {@link FfbMobileClient} as String, including internal information like webClient, user, loginstatus (but not the PIN), and
   * the GSON object.
   */
  @Override
  public String toString() {
    /* Return interesting fields, but do not return the pin. */

    return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
        .add("webclienthelper=" + webclienthelper)
        .add("pin=" + "*****")
        .add("user=" + user)
        .toString();
  }

}
