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

import de.bmarwell.ffb.depot.client.err.FfbClientError;
import de.bmarwell.ffb.depot.client.json.FfbDispositionenResponse;
import de.bmarwell.ffb.depot.client.json.FfbPerformanceResponse;
import de.bmarwell.ffb.depot.client.json.LoginResponse;
import de.bmarwell.ffb.depot.client.json.MyFfbResponse;
import de.bmarwell.ffb.depot.client.value.FfbLoginKennung;
import de.bmarwell.ffb.depot.client.value.FfbPin;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ServiceLoader;

/**
 * Main class of the mobile client. It stores the current connection status (logged in etc.) and provides access methods for
 * various account information.
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
   * Path to Logout.
   */
  private static final String PATH_LOGOUT = "/de/mobile/account/logout.page";

  /**
   * The web client used by this class to perform http requests.
   */
  private final WebClient webClient;

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
  private Optional<LoginResponse> login = Optional.<LoginResponse>absent();
  /**
   * The URL to the MyFFB-Page, created in the constructor.
   */
  private final URL urlMyffb;
  /**
   * The URL to the Login-Page, created in the constructor.
   */
  private final URL urlLogin;
  /**
   * The URL to the Performance-Page, created in the constructor.
   */
  private final URL urlPerformance;
  /**
   * The URL to the Dispositions-Page, created in the constructor.
   */
  private final URL urlDispositions;

  /**
   * The URL to the Logout-Page, created in the constructor.
   */
  private URL urlLogout;

  /**
   * A GsonBuilder holds type information and can be used as a factory to create
   * Gson objects.
   */
  private final GsonBuilder gsonBuilder;

  /**
   * Konstruktor for tests and internal uses only. PLease use {@link #FfbMobileClient(FfbLoginKennung, FfbPin)}
   * instead.
   *
   * <p>Wird der Client wie hier ohne User und Pin erstellt, kann gar nichts klappen.</p>
   *
   * @throws MalformedURLException
   *           Interner Fehler beim Erstellen der URLs.
   */
  public FfbMobileClient() throws MalformedURLException {
    this.webClient = new WebClient();
    webClient.getCookieManager().setCookiesEnabled(true);

    urlMyffb = new URL(DOMAIN + PATH_DEPOT);
    urlLogin = new URL(DOMAIN + PATH_LOGIN);
    urlPerformance = new URL(DOMAIN + PATH_PERFORMANCE);
    urlDispositions = new URL(DOMAIN + PATH_DISPOSITIONEN);
    urlLogout = new URL(DOMAIN + PATH_LOGOUT);

    gsonBuilder = initGsonBuilder();
  }

  /**
   * Erstellt ein Retriever, der von der FFB über die Mobile-Schnittstelle Daten empfängt.
   *
   * @param user
   *          Der Login. Meistens die Depotnummer. Bei mehreren Depots mit selbem Login einfach ebenfalls das Login.
   * @param pin
   *          das Passwort fürs Login.
   * @throws MalformedURLException
   *           Fehler beim erstellen der URLs.
   */
  public FfbMobileClient(FfbLoginKennung user, FfbPin pin) throws MalformedURLException {
    this();
    this.user = user;
    this.pin = pin;
  }

  /**
   * Factory method to create a GsonBuilder which knows the immutable types.
   *
   * @return a gson builder with typeadapters registered.
   */
  public static GsonBuilder initGsonBuilder() {
    final GsonBuilder localBuilder = new GsonBuilder();

    for (final TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
      localBuilder.registerTypeAdapterFactory(factory);
    }

    return localBuilder;
  }

  /**
   * Diese Methode ermittelt die aktuellen Depotbestände und weitere Informationen aus der Gesamtheit aller Depots.
   *
   * <p><b>Hinweis:</b> Zuvor muss ein {@link #logon()} aufgerufen worden sein, sonst gibt es eine
   * {@link IllegalStateException}.
   *
   * @return die Depotinfo.
   * @throws FfbClientError
   *           Error while getting account data.
   */
  public MyFfbResponse fetchAccountData() throws FfbClientError {
    Preconditions.checkState(login.isPresent(), NOT_USED_LOGIN_METHOD_BEFORE);
    Preconditions.checkState(login.get().isLoggedIn(), USER_COULD_NOT_LOG_IN_CHECK_CREDENTIALS);

    MyFfbResponse bestandsResponse = null;

    try {
      final Page myFfbPage = webClient.getPage(urlMyffb);

      /* Read json response */
      final JsonReader reader = new JsonReader(
          new InputStreamReader(myFfbPage.getWebResponse().getContentAsStream(), StandardCharsets.UTF_8));
      final Gson gson = gsonBuilder.create();
      bestandsResponse = gson.fromJson(reader, MyFfbResponse.class);
    } catch (FailingHttpStatusCodeException fsce) {
      LOG.error("Error with reading account information (http statuscode). Are you logged in?", fsce);
      throw new FfbClientError("Error with reading account information (http statuscode). Are you logged in?", fsce);
    } catch (IOException ioe) {
      LOG.error("Error with reading account information. Could not read stream.", ioe);
      throw new FfbClientError("Error with reading account information. Could not read stream.", ioe);
    }

    return bestandsResponse;
  }

  /**
   * Login über Cookies.
   *
   * @throws FfbClientError
   *           Error logging in. Wrong login data?
   */
  public void logon() throws FfbClientError {
    try {
      /*
       * For the original login request, multiple headers are being set.
       */
      final WebRequest requestSettings = new WebRequest(urlLogin, HttpMethod.POST);
      requestSettings.setAdditionalHeader("Accept", "application/json; q=0.01");
      requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      requestSettings.setAdditionalHeader("Accept-Language", "en-GB,en-US,en;q=0.8");
      requestSettings.setAdditionalHeader("Accept-Encoding", "gzip,deflate");
      requestSettings.setAdditionalHeader("Accept-Charset", "utf-8;q=0.7,*;q=0.3");
      requestSettings.setAdditionalHeader("X-Requested-With", "hibiscus.ffb.scraper");
      requestSettings.setAdditionalHeader("User-Agent", "hibiscus.ffb.scraper");
      requestSettings.setAdditionalHeader("Cache-Control", "no-cache");
      requestSettings.setAdditionalHeader("Pragma", "no-cache");
      requestSettings.setAdditionalHeader("Origin", "file://");

      /* pass user and login as string like 'login=user&password=pin'. */
      requestSettings.setRequestBody("login=" + user.getLoginKennung() + "&password=" + pin.getPinAsString());
      final Page redirectPage = webClient.getPage(requestSettings);
      redirectPage.getWebResponse();
      final JsonReader reader = new JsonReader(
          new InputStreamReader(redirectPage.getWebResponse().getContentAsStream(), StandardCharsets.UTF_8));
      final Gson gson = gsonBuilder.create();
      final LoginResponse response = gson.fromJson(reader, LoginResponse.class);
      this.login = Optional.<LoginResponse>of(response);
    } catch (FailingHttpStatusCodeException fsce) {
      LOG.error(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
      throw new FfbClientError(ERROR_WITH_LOGIN_HTTP_STATUSCODE, fsce);
    } catch (IOException ioe) {
      LOG.error(ERROR_RESPONSE_STREAM, ioe);
      throw new FfbClientError(ERROR_RESPONSE_STREAM, ioe);
    }
  }

  /**
   * Gibt die Performance für alle Depots dieses Logins aus.
   *
   * @return ein {@link FfbPerformanceResponse} mit einigen Performance-Infos.
   * @throws FfbClientError
   *           Falls es zuvor keinen (derzeit noch) gültigen Login gab.
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
   *
   * @throws FfbClientError
   *           login error etc.
   * @throws IllegalStateException
   *           if not logged in.
   */
  public FfbDispositionenResponse getDispositionen() throws FfbClientError {
    Preconditions.checkState(login.isPresent(), NOT_USED_LOGIN_METHOD_BEFORE);
    Preconditions.checkState(login.get().isLoggedIn(), USER_COULD_NOT_LOG_IN_CHECK_CREDENTIALS);

    try {
      final Page umsatzPage = webClient.getPage(urlDispositions);

      /* Read json response */
      final JsonReader reader = new JsonReader(
          new InputStreamReader(umsatzPage.getWebResponse().getContentAsStream(), StandardCharsets.UTF_8));
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
   * Die bei der {@link #logon}-Methode erhaltenen Informationen. Entspricht {@link Optional#absent()}, falls das Login nicht
   * erfolgreich war, oder es nicht durchgeführt wurde.
   *
   * @return Ein LoginResponse im Optional, falls zuvor {@link #logon()} erfolgreich durchgeführt wurde.
   */
  public Optional<LoginResponse> loginInformation() {
    return login;
  }

  /**
   * Returns the {@link FfbMobileClient} as String, including
   * internal information like webClient, user, loginstatus (but not the PIN),
   * and the GSON object.
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
