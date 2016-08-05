package de.bmarwell.ffb.depot.client;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FfbMobileDepotwertRetriever {

  private static final String DOMAIN = "https://www.fidelity.de/";
  private static final String PATH_LOGIN = "de/mobile/MyFFB/account/userLogin.page";
  private static final String PATH_DEPOT = "de/mobile/MyFFB/account/MyFFB.page";

  private final WebClient webClient;

  private byte[] pin = new String("").getBytes();

  private String user = new String("");

  private double depotwert = 0;

  private Optional<LoginResponse> login = Optional.<LoginResponse>absent();
  private Optional<MyFfbResponse> myFfbInfo = Optional.<MyFfbResponse>absent();
  private String depotnummer;

  public FfbMobileDepotwertRetriever() {
    this.webClient = new WebClient();
    webClient.getCookieManager().setCookiesEnabled(true);
  }

  /**
   * Erstellt ein Retriever, der von der FFB über die Mobile-Schnittstelle Daten empfängt.
   *
   * @param user
   *          Der Login. Meistens die Depotnummer. Bei mehreren Depots mit selbem Login einfach ebenfalls das Login.
   * @param pin
   *          das Passwort fürs Login.
   * @param depotnummer
   *          Die Depotnummer, für die der Depotbestand abgefragt werden soll. Ein Login kann ggf. mehrere Depots sehen.
   */
  public FfbMobileDepotwertRetriever(String user, String pin, String depotnummer) {
    this();
    this.user = user;
    this.pin = pin.getBytes();
    this.depotnummer = depotnummer;
  }

  /**
   * Die Methode, die die ganze Arbeit verrichtet: Login, Sammeln der Daten und addieren der relevanten Depotbestände.
   *
   * <p>Schmeißt RuntimeExceptions, wenn es Fehler gibt.</p>
   */
  public void synchronize() {
    try {
      /* Login, das Cookie im webClient erledigt alles weitere. */
      logon();

      /* Die eigentlichen Daten werden hier abgeholt und im myFfbInfo gespeichert. */
      fetchAccountData();

      /* Bestände aller passenden Depots im Login werden addiert. */
      setDepotwert();
    } catch (MalformedURLException mue) {
      throw new RuntimeException("Malformed URL in FFB-Project: [" + mue.getMessage() + "]. Please send this to developer.",
          mue);
    } catch (FailingHttpStatusCodeException httpStatusCodeEx) {
      throw new RuntimeException(
          "Could not connect to: [" + httpStatusCodeEx.getMessage() + "]. Please try again or send this to developer.",
          httpStatusCodeEx);
    } catch (IOException ioEx) {
      throw new RuntimeException(
          "Could not read from: [" + ioEx.getMessage() + "]. Please try again or send this to developer.", ioEx);
    }
  }

  /**
   * Bestände aller passenden Depots werden addiert und in Feld deportwert gespeichert.
   */
  private void setDepotwert() {
    Preconditions.checkState(login.isPresent(), "Not used login method before.");
    Preconditions.checkState(login.get().isLoggedIn(), "User could not log in. Check credentials.");
    Preconditions.checkState(myFfbInfo.isPresent(), "Keine Daten abgeholt!");

    double tempDepotwert = 0.00d;

    /* Es kann mehrere Depots mit der gleichen Depotnummer geben (z.B. Haupt- und VL-Depot). */
    for (FfbDepotInfo di : myFfbInfo.get().getDepots()) {
      if (!di.getDepotnummer().equals(depotnummer)) {
        /* Dieses Depot im sichtbaren Login ist ein anderes, als das für Umsätze angefordete */
        continue;
      }

      tempDepotwert += di.getBestand();
    }

    this.depotwert = tempDepotwert;
  }

  /**
   * Hier werden die eigentlichen Daten (Depotliste inkl. Bestände) geholt.
   *
   * @throws FailingHttpStatusCodeException
   *           Statuscode unschön.
   * @throws IOException
   *           Fehler beim Lesen des Response.
   */
  public void fetchAccountData() throws FailingHttpStatusCodeException, IOException {
    Preconditions.checkState(login.isPresent(), "Not used login method before.");
    Preconditions.checkState(login.get().isLoggedIn(), "User could not log in. Check credentials.");

    URL myFfbUrl = new URL(DOMAIN + PATH_DEPOT);
    Page myFfbPage = webClient.getPage(myFfbUrl);

    /* Read json response */
    JsonReader reader = new JsonReader(new InputStreamReader(myFfbPage.getWebResponse().getContentAsStream()));
    Gson gson = new Gson();
    MyFfbResponse bestandsResponse = gson.fromJson(reader, MyFfbResponse.class);
    myFfbInfo = Optional.of(bestandsResponse);
  }

  public double depotwert() {
    return this.depotwert;
  }

  /**
   * Login über Cookies.
   *
   * @throws FailingHttpStatusCodeException
   *           Statuscode unschön.
   * @throws IOException
   *           Fehler beim Lesen des HTTP-Response.
   */
  public void logon() throws FailingHttpStatusCodeException, IOException {
    URL login = new URL(DOMAIN + PATH_LOGIN);
    WebRequest requestSettings = new WebRequest(login, HttpMethod.POST);
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

    requestSettings.setRequestBody("login=" + user + "&password=" + new String(pin));
    Page redirectPage = webClient.getPage(requestSettings);
    redirectPage.getWebResponse();
    JsonReader reader = new JsonReader(new InputStreamReader(redirectPage.getWebResponse().getContentAsStream()));
    Gson gson = new Gson();
    LoginResponse response = gson.fromJson(reader, LoginResponse.class);
    this.login = Optional.<LoginResponse>of(response);

  }

  public Optional<LoginResponse> loginInformation() {
    return login;
  }

  public Optional<MyFfbResponse> depotInformation() {
    return myFfbInfo;
  }

}
