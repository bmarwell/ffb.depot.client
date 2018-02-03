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

package de.bmarwell.ffb.depot.client;

import static java.util.Objects.requireNonNull;

import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbDispositionenResponse;
import de.bmarwell.ffb.depot.client.json.FfbPerformanceResponse;
import de.bmarwell.ffb.depot.client.json.FfbUmsatzResponse;
import de.bmarwell.ffb.depot.client.json.LoginResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.util.JacksonMessageBodyReader;
import de.bmarwell.ffb.depot.client.value.FfbAuftragsTyp;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class of the mobile client. It stores the current connection status (logged in etc.) and provides access methods for various account
 * information.
 */
public class FfbMobileClient {


  /**
   * Tried to execute action without prior login.
   */
  private static final String NOT_USED_LOGIN_METHOD_BEFORE = "Not used login method before.";

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(FfbMobileClient.class);

  /**
   * Path to login (DOMAIN + PATH).
   */
  private static final String PATH_LOGIN = "/de/mobile/MyFFB/account/userLogin.page";
  /**
   * Path to depot overview (DOMAIN + PATH).
   */
  private static final String PATH_DEPOT = "/de/mobile/MyFFB/account/MyFFB.page";
  /**
   * Path to depot performance (DOMAIN + PATH).
   */
  private static final String PATH_PERFORMANCE = "/de/mobile/account/performance.page";
  /**
   * Path to dispositions (DOMAIN + PATH).
   */
  private static final String PATH_DISPOSITIONEN = "/de/mobile/account/dispositionen.page";

  /**
   * Path to Umsaetze (transactions).
   */
  private static final String PATH_UMSAETZE = "/de/mobile/account/umsaetze.page";

  /**
   * Path to Logout.
   */
  private static final String PATH_LOGOUT = "/de/mobile/account/logout.page";

  /**
   * The web client used by this class to perform http requests.
   */
  private final Client webClient = ClientBuilder.newClient()
      .register(JacksonMessageBodyReader.class);
  /**
   * Base URL including protocol and domain for access.
   */
  private final URI basedomain;
  /**
   * Saved PIN for use in {@link #logon()}-method.
   */
  private FfbPin pin = FfbPin.of("");
  /**
   * User for use in {@link #logon()}-method.
   */
  private FfbLoginKennung user = FfbLoginKennung.of("");
  /**
   * Holding information about successful login.
   */
  private LoginResponse login = LoginResponse.builder()
      .agbAgreed(false)
      .isLoggedIn(false)
      .build();

  private Map<String, NewCookie> cookies = Collections.unmodifiableMap(new HashMap<>());

  /**
   * Konstruktor for tests and internal uses only. PLease use {@link #FfbMobileClient(FfbLoginKennung, FfbPin)} instead.
   *
   * <p>Wird der Client wie hier ohne User und Pin erstellt, kann gar nichts klappen.</p>
   */
  public FfbMobileClient(final FfbClientConfiguration config) {
    basedomain = config.getBaseUrl();
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
   */
  public FfbMobileClient(final FfbLoginKennung user, final FfbPin pin, final FfbClientConfiguration config) {
    basedomain = config.getBaseUrl();
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
    requireNonNull(login, NOT_USED_LOGIN_METHOD_BEFORE);

    if (!isLoggedIn()) {
      throw new IllegalStateException("Not logged in!");
    }

    LOG.info("Calling URI: [{}].", getMyFfbUri().toASCIIString());
    Builder target = webClient
        .target(getMyFfbUri())
        .request(MediaType.APPLICATION_JSON_TYPE);

    for (final Entry<String, NewCookie> cookie : cookies.entrySet()) {
      target = target.cookie(cookie.getKey(), cookie.getValue().toCookie().getValue());
    }

    final Response response = target
        .get();

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
    final Form form = new Form();
    form.param("login", user.getLoginKennung());
    form.param("password", pin.getPinAsString());

    LOG.info("Calling URI: [{}].", getUriLogin().toASCIIString());
    final Response loginResponse = webClient
        .target(getUriLogin())
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Accept", "application/json;charset=utf-8")
        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8" )
        .header("Accept-Charset", "utf-8;q=0.7,*;q=0.3" )
        .header("X-Requested-With", "hibiscus.ffb.scraper")
        .header("User-Agent", "hibiscus.ffb.scraper" )
        .post(Entity.form(form));

    final LoginResponse ffbLogin = loginResponse.readEntity(LoginResponse.class);
    login = ffbLogin;

    if (!ffbLogin.isLoggedIn() || !ffbLogin.getErrormessage().orElse("").isEmpty()) {
      LOG.error("Konnte Client nicht einloggen! => [{}]", ffbLogin);
      cookies = Collections.unmodifiableMap(new HashMap<>());

      return;
    }

    cookies = loginResponse.getCookies();
    LOG.debug("Cookie names: [{}].", cookies.keySet());
  }

  /**
   * Gibt die Performance für alle Depots dieses Logins aus.
   *
   * @return ein {@link FfbPerformanceResponse} mit einigen Performance-Infos.
   * @throws FfbClientError Falls es zuvor keinen (derzeit noch) gültigen Login gab.
   * @throws IllegalStateException if not logged in.
   */
  public FfbPerformanceResponse getPerformance() throws FfbClientError {
    checkLoggedIn();

    Builder request = webClient
        .target(getUriPerformance())
        .request(MediaType.APPLICATION_JSON_TYPE);

    for (final Map.Entry<String, NewCookie> cookie : cookies.entrySet()) {
      LOG.debug("Adding cookie: [{}] => [{}].", cookie.getKey(), cookie.getValue().toCookie());
      request = request.cookie(cookie.getValue().toCookie());
    }

    final Response response = request
        .get();

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
    checkLoggedIn();

    Builder builder = webClient.target(getUriDispositionen())
        .request(MediaType.APPLICATION_JSON_TYPE);

    for (final NewCookie cookie : cookies.values()) {
      builder = builder.cookie(cookie);
    }

    final Response response = builder
        .get();

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
    checkLoggedIn();

    requireNonNull(auftragsTyp, "auftragsTyp");
    requireNonNull(auftragsTyp, "from");
    requireNonNull(auftragsTyp, "until");

    /* Make sure, the from date is not older than the other one */
    if (LocalDate.now().minusMonths(5).minusDays(15).isAfter(from)) {
      throw new IllegalArgumentException("Period may not exceed 5 Months, 15 Days ago from now.");
    }

    final String datumRangeString = FfbDepotUtils.convertDateRangeToGermanDateRangeString(from, until);
    final Response response = webClient.target(getUriUmsaetze())
        .queryParam("auftragstyp", auftragsTyp.toString())
        .queryParam("datumsauswahl", datumRangeString)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    final FfbUmsatzResponse umsatzResponse = response.readEntity(FfbUmsatzResponse.class);

    LOG.debug("Response Umsätze für Datumsbereich [{}] => [{}].", datumRangeString, umsatzResponse);

    return umsatzResponse;
  }

  public boolean logout() throws FfbClientError {
    checkLoggedIn();

    login = LoginResponse.builder()
        .isLoggedIn(false)
        .agbAgreed(false)
        .build();

    final Response response = webClient.target(getUriLogout())
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    return Status.OK.getStatusCode() == response.getStatus();
  }

  /**
   * Die bei der {@link #logon}-Methode erhaltenen Informationen.
   *
   * @return Ein LoginResponse.
   */
  public LoginResponse loginInformation() {
    return login;
  }


  public Map<String, NewCookie> getCookies() {
    return cookies;
  }

  private void checkLoggedIn() {
    if (cookies.isEmpty() || !isLoggedIn()) {
      throw new IllegalStateException("Not logged in");
    }
  }


  public boolean isLoggedIn() {
    return login.isLoggedIn() && !cookies.isEmpty();
  }

  public URI getMyFfbUri() {
    return UriBuilder.fromUri(basedomain)
        .path(PATH_DEPOT)
        .build();
  }

  public URI getUriLogin() {
    return UriBuilder.fromUri(basedomain)
        .path(PATH_LOGIN)
        .build();
  }

  public URI getUriPerformance() {
    return UriBuilder.fromUri(basedomain)
        .path(PATH_PERFORMANCE)
        .build();
  }

  public URI getUriDispositionen() {
    return UriBuilder.fromUri(basedomain)
        .path(PATH_DISPOSITIONEN)
        .build();
  }

  public URI getUriUmsaetze() {
    return UriBuilder.fromUri(basedomain)
        .path(PATH_UMSAETZE)
        .build();
  }

  public URI getUriLogout() {
    return UriBuilder.fromUri(basedomain)
        .path(PATH_LOGOUT)
        .build();
  }

  /**
   * Returns the {@link FfbMobileClient} as String, including internal information like webClient, user, loginstatus (but not the PIN), and
   * the GSON object.
   */
  @Override
  public String toString() {
    /* Return interesting fields, but do not return the pin. */

    return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
        .add("webClient=" + webClient)
        .add("basedomain=" + basedomain)
        .add("pin=" + "*****")
        .add("user=" + user)
        .add("login=" + loginInformation())
        .add("cookies=" + cookies)
        .toString();
  }

}
