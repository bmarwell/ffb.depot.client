package de.bmarwell.ffb.depot.client;

import de.bmarwell.ffb.depot.client.err.FfbClientError;
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
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FfbMobileClient {

  private static final Logger LOG = LoggerFactory.getLogger(FfbMobileClient.class);

  private static final String DOMAIN = "https://www.fidelity.de/";
  private static final String PATH_LOGIN = "de/mobile/MyFFB/account/userLogin.page";
  private static final String PATH_DEPOT = "de/mobile/MyFFB/account/MyFFB.page";
  private static final String PATH_PERFORMANCE = "/de/mobile/account/performance.page";

  private final WebClient webClient;

  private FfbPin pin = FfbPin.of("");

  private FfbLoginKennung user = FfbLoginKennung.of("");

  private Optional<LoginResponse> login = Optional.<LoginResponse>absent();
  private final URL urlMyffb;
  private final URL urlLogin;
  private final URL urlPerformance;

  /**
   * Konstruktor für Tests und interne Verwendung. Bitte stattdessen {@link #FfbMobileClient(FfbLoginKennung, FfbPin)}
   * verwenden.
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
   * Hier werden die eigentlichen Daten (Depotliste inkl. Bestände) geholt.
   *
   * @return die Depotinfo.
   * @throws FfbClientError
   *           Error while getting account data.
   */
  public MyFfbResponse fetchAccountData() throws FfbClientError {
    Preconditions.checkState(login.isPresent(), "Not used login method before.");
    Preconditions.checkState(login.get().isLoggedIn(), "User could not log in. Check credentials.");

    MyFfbResponse bestandsResponse = null;

    try {
      Page myFfbPage = webClient.getPage(urlMyffb);

      /* Read json response */
      JsonReader reader = new JsonReader(new InputStreamReader(myFfbPage.getWebResponse().getContentAsStream()));
      Gson gson = new Gson();
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
      WebRequest requestSettings = new WebRequest(urlLogin, HttpMethod.POST);
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

      requestSettings.setRequestBody("login=" + user.getLoginKennung() + "&password=" + pin.getPinAsString());
      Page redirectPage = webClient.getPage(requestSettings);
      redirectPage.getWebResponse();
      JsonReader reader = new JsonReader(new InputStreamReader(redirectPage.getWebResponse().getContentAsStream()));
      Gson gson = new Gson();
      LoginResponse response = gson.fromJson(reader, LoginResponse.class);
      this.login = Optional.<LoginResponse>of(response);
    } catch (FailingHttpStatusCodeException fsce) {
      LOG.error("Error with login (http statuscode). Please submit a bug.", fsce);
      throw new FfbClientError("Error with login (http statuscode). Please submit a bug.", fsce);
    } catch (IOException ioe) {
      LOG.error("Error logging in while reading the response stream. Please submit a bug.", ioe);
      throw new FfbClientError("Error logging in while reading the response stream. Please submit a bug.", ioe);
    }
  }

  public FfbPerformanceResponse getPerformance() throws FfbClientError {
    Preconditions.checkState(login.isPresent(), "Not used login method before.");
    Preconditions.checkState(login.get().isLoggedIn(), "User could not log in. Check credentials.");

    FfbPerformanceResponse performanceResponse = null;

    try {
      Page performancePage = webClient.getPage(urlPerformance);

      /* Read json response */
      JsonReader reader = new JsonReader(new InputStreamReader(performancePage.getWebResponse().getContentAsStream()));
      Gson gson = new Gson();

      performanceResponse = gson.fromJson(reader, FfbPerformanceResponse.class);
    } catch (FailingHttpStatusCodeException fsce) {
      LOG.error("Error with login (http statuscode). Please submit a bug.", fsce);
      throw new FfbClientError("Error with login (http statuscode). Please submit a bug.", fsce);
    } catch (IOException ioe) {
      LOG.error("Error logging in while reading the response stream. Please submit a bug.", ioe);
      throw new FfbClientError("Error logging in while reading the response stream. Please submit a bug.", ioe);
    }

    return performanceResponse;
  }

  public Optional<LoginResponse> loginInformation() {
    return login;
  }

}
