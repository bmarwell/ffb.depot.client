package de.bmarwell.ffb.depot.client.util;

import static org.slf4j.LoggerFactory.getLogger;

import de.bmarwell.ffb.depot.client.FfbDepotUtils;
import de.bmarwell.ffb.depot.client.json.LoginResponse;
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
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;

/**
 * Helper methods for web client.
 */
public final class WebClientHelper {

  private static final Logger LOG = getLogger(WebClientHelper.class);

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
  private final String useragent;
  /**
   * Holding information about successful login.
   */
  private LoginResponse login = LoginResponse.builder()
      .agbAgreed(false)
      .isLoggedIn(false)
      .build();
  private Map<String, NewCookie> cookies = Collections.unmodifiableMap(new HashMap<>());

  public WebClientHelper(final URI basedomain, final String userAgent) {
    this.basedomain = basedomain;
    this.useragent = userAgent;
  }

  public Builder getClientBuilderLogin() {
    final Map<String, String> additionalHeaders = new ConcurrentHashMap<>();
    additionalHeaders.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

    return getClientBuilder(getUriLogin(), Collections.emptyMap(), additionalHeaders);
  }

  public Builder getClientBuilder(final URI uriToCall) {
    return getClientBuilder(uriToCall, Collections.emptyMap(), Collections.emptyMap());
  }

  public Builder getClientBuilder(
      final URI uriToCall,
      final Map<String, String> additionalQueryParams,
      final Map<String, String> additionalHeaders) {
    WebTarget target = webClient.target(uriToCall);

    for (final Entry<String, String> queryParam : additionalQueryParams.entrySet()) {
      target = target.queryParam(queryParam.getKey(), queryParam.getValue());
    }

    Builder clientBuilder = target
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Accept", "application/json;charset=utf-8")
        .header("Accept-Charset", "utf-8;q=0.7,*;q=0.3")
        .header("X-Requested-With", this.useragent)
        .header("User-Agent", this.useragent);

    for (final Map.Entry<String, NewCookie> cookie : cookies.entrySet()) {
      LOG.debug("Adding cookie: [{}] => [{}].", cookie.getKey(), cookie.getValue().toCookie());
      clientBuilder = clientBuilder.cookie(cookie.getValue().toCookie());
    }

    for (final Entry<String, String> header : additionalHeaders.entrySet()) {
      clientBuilder = clientBuilder.header(header.getKey(), header.getValue());
    }

    return clientBuilder;
  }


  public Map<String, NewCookie> getCookies() {
    return cookies;
  }

  public LoginResponse loginInformation() {
    return this.login;
  }

  public void checkLoggedIn() {
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

  public void login(final FfbLoginKennung user, final FfbPin pin) {
    if (isLoggedIn()) {
      throw new IllegalStateException("Please log out first!");
    }

    LOG.info("Calling URI: [{}].", getUriLogin().toASCIIString());
    final Builder clientBuilder = getClientBuilderLogin();

    final Form form = new Form();
    form.param("login", user.getLoginKennung());
    form.param("password", pin.getPinAsString());

    final Response loginResponse = clientBuilder.post(Entity.form(form));

    if (loginResponse.getStatus() != Status.OK.getStatusCode()) {
      final String ffbLogin = loginResponse.readEntity(String.class);
      LOG.error("Konnte Client nicht einloggen! => SC = [{}]", ffbLogin);
      cookies = Collections.unmodifiableMap(new HashMap<>());

      return;
    }

    final LoginResponse ffbLogin = loginResponse.readEntity(LoginResponse.class);

    if (!ffbLogin.isLoggedIn() || !ffbLogin.getErrormessage().orElse("").isEmpty()) {
      LOG.error("Konnte Client nicht einloggen! => [{}]", ffbLogin);
      cookies = Collections.unmodifiableMap(new HashMap<>());

      return;
    }

    login = ffbLogin;
    cookies = loginResponse.getCookies();
    LOG.debug("Cookie names: [{}].", cookies.keySet());
  }

  public Invocation accountData() {
    if (!isLoggedIn()) {
      throw new IllegalStateException("Not logged in!");
    }

    return getClientBuilder(getMyFfbUri()).buildGet();
  }


  public Invocation performance() {
    if (!isLoggedIn()) {
      throw new IllegalStateException("Not logged in!");
    }

    return getClientBuilder(getUriPerformance()).buildGet();
  }

  public Invocation dispositionen() {
    if (!isLoggedIn()) {
      throw new IllegalStateException("Not logged in!");
    }

    return getClientBuilder(getUriDispositionen()).buildGet();
  }

  public Invocation umsaetze(final FfbAuftragsTyp auftragsTyp, final LocalDate from, final LocalDate until) {
    if (!isLoggedIn()) {
      throw new IllegalStateException("Not logged in!");
    }

    final String datumRangeString = FfbDepotUtils.convertDateRangeToGermanDateRangeString(from, until);

    final ConcurrentHashMap<String, String> queryParams = new ConcurrentHashMap<>();
    queryParams.put("datumsauswahl", datumRangeString);
    queryParams.put("auftragstyp", auftragsTyp.toString());

    return getClientBuilder(
        getUriUmsaetze(),
        queryParams,
        Collections.emptyMap())
        .buildGet();
  }

  public boolean logout() {
    login = LoginResponse.builder()
        .isLoggedIn(false)
        .agbAgreed(false)
        .build();
    cookies = Collections.emptyMap();

    final Response response = getClientBuilder(getUriLogout()).get();

    return Status.OK.getStatusCode() == response.getStatus();
  }

  @Override
  public String toString() {
    /* Return interesting fields, but do not return the pin. */

    return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
        .add("webClient=" + webClient)
        .add("basedomain=" + basedomain)
        .add("login=" + login)
        .add("cookies=" + cookies)
        .toString();
  }

}
