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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
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
   * Wrong credentials?
   */
  private static final String USER_COULD_NOT_LOG_IN_CHECK_CREDENTIALS = "User could not log in. Check credentials.";

  /**
   * Tried to execute action without prior login.
   */
  private static final String NOT_USED_LOGIN_METHOD_BEFORE = "Not used login method before.";

  /**
   * Error to be logged when reading the response stream.
   */
  private static final String ERROR_RESPONSE_STREAM = "Error logging in while reading the response stream. Please submit a bug.";

  /**
   * Error message on invalid status code.
   */
  private static final String ERROR_WITH_LOGIN_HTTP_STATUSCODE = "Error with login (http statuscode). Please submit a bug.";

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(FfbMobileClient.class);

  /**
   * Base URL including protocol and domain for access.
   */
  private static final String DOMAIN = "https://www.fidelity.de";
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
  private final Client webClient;

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
  private Optional<LoginResponse> login = Optional.<LoginResponse>empty();
  /**
   * The URL to the MyFFB-Page, created in the constructor.
   */
  private static final URI urlMyffb = URI.create(DOMAIN + PATH_DEPOT);
  /**
   * The URL to the Login-Page, created in the constructor.
   */
  private static final URI urlLogin = URI.create(DOMAIN + PATH_LOGIN);
  /**
   * The URL to the Performance-Page, created in the constructor.
   */
  private static final URI urlPerformance = URI.create(DOMAIN + PATH_PERFORMANCE);
  /**
   * The URL to the Dispositions-Page, created in the constructor.
   */
  private static final URI urlDispositions = URI.create(DOMAIN + PATH_DISPOSITIONEN);

  /**
   * The URL to the Umsaetze-Page, created in the constructor.
   */
  private static final URI urlUmsaetze = URI.create(DOMAIN + PATH_UMSAETZE);

  /**
   * The URL to the Logout-Page, created in the constructor.
   */
  private static final URI urlLogout = URI.create(DOMAIN + PATH_LOGOUT);

  private Map<String, NewCookie> cookies = Collections.unmodifiableMap(new HashMap<>());

  private boolean isLoggedIn = false;

  /**
   * Konstruktor for tests and internal uses only. PLease use {@link #FfbMobileClient(FfbLoginKennung, FfbPin)} instead.
   *
   * <p>Wird der Client wie hier ohne User und Pin erstellt, kann gar nichts klappen.</p>
   *
   * @throws MalformedURLException Interner Fehler beim Erstellen der URLs.
   */
  public FfbMobileClient() {
    this.webClient = ClientBuilder.newClient()
        .register(JacksonMessageBodyReader.class);
  }

  /**
   * Erstellt ein Retriever, der von der FFB über die Mobile-Schnittstelle Daten empfängt.
   *
   * @param user Der Login. Meistens die Depotnummer. Bei mehreren Depots mit selbem Login einfach ebenfalls das Login.
   * @param pin das Passwort fürs Login.
   */
  public FfbMobileClient(FfbLoginKennung user, FfbPin pin) {
    this();
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
    requireNonNull(login.isPresent(), NOT_USED_LOGIN_METHOD_BEFORE);

    if (!isLoggedIn) {
      throw new IllegalStateException("Not logged in!");
    }

    final Builder target = webClient
        .target(urlMyffb)
        .request(MediaType.APPLICATION_JSON_TYPE);

    cookies.entrySet().stream()
        .map(cookieEntry -> target.cookie(cookieEntry.getKey(),cookieEntry.getValue().toCookie().getValue()));

    final Response response = target

        .get();

    MyFfbResponse bestandsResponse = response.readEntity(MyFfbResponse.class);

    LOG.debug("BestandsResponse: [{}].", bestandsResponse);

    return bestandsResponse;
  }

  /**
   * Login über Cookies.
   *
   * @throws FfbClientError Error logging in. Wrong login data?
   */
  public void logon() throws FfbClientError {
    Form form = new Form();
    form.param("login", user.getLoginKennung());
    form.param("password", pin.getPinAsString());

    final Response loginResponse = webClient
        .target(urlLogin)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Accept", "application/json;charset=utf-8")
        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8" )
        .header("Accept-Charset", "utf-8;q=0.7,*;q=0.3" )
        .header("X-Requested-With", "hibiscus.ffb.scraper")
        .header("User-Agent", "hibiscus.ffb.scraper" )
        .post(Entity.form(form));

    final LoginResponse ffbLogin = loginResponse.readEntity(LoginResponse.class);

    if (!ffbLogin.isLoggedIn() || ffbLogin.getErrormessage().isPresent()) {
      LOG.error("Konnte Client nicht einloggen!", ffbLogin);
      this.cookies = Collections.unmodifiableMap(new HashMap<>());
      this.isLoggedIn = false;

      return;
    }

    cookies = loginResponse.getCookies();
    this.isLoggedIn = true;
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
    Preconditions.checkState(login.isPresent(), NOT_USED_LOGIN_METHOD_BEFORE);
    Preconditions.checkState(login.get().isLoggedIn(), USER_COULD_NOT_LOG_IN_CHECK_CREDENTIALS);

    FfbPerformanceResponse performanceResponse = null;

    try {
      final Page performancePage = webClient.getPage(urlPerformance);

      /* Read json response */
      final JsonReader reader = new JsonReader(
          new InputStreamReader(performancePage.getWebResponse().getContentAsStream(), StandardCharsets.UTF_8));
      final Gson gson = gsonBuilder.create();

      performanceResponse = gson.fromJson(reader, FfbPerformanceResponse.class);
    } catch (FailingHttpStatusCodeException fsce) {
      LOG.error(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
      throw new FfbClientError(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
    } catch (IOException ioe) {
      LOG.error(ERROR_RESPONSE_STREAM, ioe);
      throw new FfbClientError(ERROR_RESPONSE_STREAM, ioe);
    }

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
    Preconditions.checkState(login.isPresent(), NOT_USED_LOGIN_METHOD_BEFORE);
    Preconditions.checkState(login.get().isLoggedIn(), USER_COULD_NOT_LOG_IN_CHECK_CREDENTIALS);

    try {
      final Page dispositionenPage = webClient.getPage(urlDispositions);

      /* Read json response */
      final JsonReader reader = new JsonReader(
          new InputStreamReader(dispositionenPage.getWebResponse().getContentAsStream(), StandardCharsets.UTF_8));
      final Gson gson = gsonBuilder.create();

      return gson.fromJson(reader, FfbDispositionenResponse.class);
    } catch (FailingHttpStatusCodeException fsce) {
      LOG.error(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
      throw new FfbClientError(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
    } catch (IOException ioe) {
      LOG.error(ERROR_RESPONSE_STREAM, ioe);
      throw new FfbClientError(ERROR_RESPONSE_STREAM, ioe);
    }
  }

  public FfbUmsatzResponse getUmsaetze(FfbAuftragsTyp auftragsTyp, LocalDate from, LocalDate until) throws FfbClientError {
    if (!isLoggedIn) {
      throw new IllegalStateException("Not logged in");
    }

    Preconditions.checkNotNull(auftragsTyp, "auftragsTyp");
    Preconditions.checkNotNull(auftragsTyp, "from");
    Preconditions.checkNotNull(auftragsTyp, "until");

    /* Make sure, the from date is not older than the other one */
    Preconditions.checkArgument(LocalDate.now().minusMonths(5).minusDays(15).isBefore(from),
        "Period may not exceed 5 Months, 15 Days ago from now.");

    try {
      WebRequest requestSettings = new WebRequest(urlUmsaetze, HttpMethod.GET);
      ImmutableList<NameValuePair> queryParameters = ImmutableList.<NameValuePair>of(
          new NameValuePair("auftragstyp", auftragsTyp.toString()),
          new NameValuePair("datumsauswahl", FfbDepotUtils.convertDateRangeToGermanDateRangeString(from, until)));
      requestSettings.setRequestParameters(queryParameters);

      LOG.debug("Requesting URL: [{}].", requestSettings.getRequestParameters());

      final Page umsatzPage = webClient.getPage(requestSettings);

      /* Read json response */
      final JsonReader reader = new JsonReader(
          new InputStreamReader(umsatzPage.getWebResponse().getContentAsStream(), StandardCharsets.UTF_8));
      final Gson gson = gsonBuilder.create();

      return gson.fromJson(reader, FfbUmsatzResponse.class);
    } catch (FailingHttpStatusCodeException fsce) {
      LOG.error(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
      throw new FfbClientError(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
    } catch (IOException ioe) {
      LOG.error(ERROR_RESPONSE_STREAM, ioe);
      throw new FfbClientError(ERROR_RESPONSE_STREAM, ioe);
    }
  }

  public boolean logout() throws FfbClientError {
    Preconditions.checkState(login.isPresent(), NOT_USED_LOGIN_METHOD_BEFORE);
    Preconditions.checkState(login.get().isLoggedIn(), USER_COULD_NOT_LOG_IN_CHECK_CREDENTIALS);

    this.login = Optional.absent();

    try {
      final Page umsatzPage = webClient.getPage(urlLogout);

      return umsatzPage.getWebResponse().getStatusCode() == 200;
    } catch (FailingHttpStatusCodeException fsce) {
      LOG.error(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
      throw new FfbClientError(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
    } catch (IOException ioe) {
      LOG.error(ERROR_RESPONSE_STREAM, ioe);
      throw new FfbClientError(ERROR_RESPONSE_STREAM, ioe);
    }
  }

  /**
   * Die bei der {@link #logon}-Methode erhaltenen Informationen. Entspricht {@link Optional#absent()}, falls das Login nicht erfolgreich
   * war, oder es nicht durchgeführt wurde.
   *
   * @return Ein LoginResponse im Optional, falls zuvor {@link #logon()} erfolgreich durchgeführt wurde.
   */
  public Optional<LoginResponse> loginInformation() {
    return login;
  }

  /**
   * Returns the {@link FfbMobileClient} as String, including internal information like webClient, user, loginstatus (but not the PIN), and
   * the GSON object.
   */
  @Override
  public String toString() {
    /* Return interesting fields, but do not return the pin. */
    return MoreObjects.toStringHelper(this)
        .add("webClient", webClient)
        .add("pin", "*****")
        .add("user", user)
        /* login is optional, the orNull() is like getOrNull() in jdk8. */
        .add("login", loginInformation().orNull())
        .add("urlMyffb", urlMyffb.toString())
        .add("gsonBuilder", gsonBuilder)
        .toString();
  }

}
