[![license](https://img.shields.io/badge/Licence-GPLv2%2B-brightgreen.svg)]() [![Build Status](https://travis-ci.org/bmhm/ffb.depot.client.svg?branch=master)](https://travis-ci.org/bmhm/ffb.depot.client) [![codecov](https://codecov.io/gh/bmhm/ffb.depot.client/branch/master/graph/badge.svg)](https://codecov.io/gh/bmhm/ffb.depot.client) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4f0a36fd479b44a590f0a8bc9e796c68)](https://www.codacy.com/app/bmarwell/ffb-depot-client?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=bmhm/ffb.depot.client&amp;utm_campaign=Badge_Grade) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.bmarwell/ffb.depot.client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.bmarwell/ffb.depot.client) [![Javadocs](http://javadoc.io/badge/de.bmarwell/ffb.depot.client.svg)](http://javadoc.io/doc/de.bmarwell/ffb.depot.client)


# ffb.depot.client
Ein Java-Client für FFB-Depots. Features: Stammdaten und Depotbestände abholen. Es war ursprünglich eine Scraping-Library für die FFB, die aus dem Projekt [Hibiscus FFB-Depot](https://github.com/bmhm/hibiscus.ffb.depot) entstanden ist. Inzwischen verwendet sie das mobile JSON-Interface von Fidelity.

(English) This used to be a FFB scraping library written in Java. Since there is now a mobile HTTP-JSON-Interface, the project switched to this kind of implementation. It is used in the Hibiscus FFB Depot-Project (see link above).

## Features

* Holen von Depotinformationen (Depotbesitzer, Depotnamen, -bestände, etc.).
* Auslesen der Depotperformance seit Einstand (Gesamtperformance aller Depots).
* Auslesen der Bestände jedes einzelnen Depots.
* TODO: Posteingang.
* TODO: Historische und offene Umsätze.

## Nutzung
Zunächst sollte eine PIN und eine Loginkennung erstellt werden:
```java
/* testlogin der FFB */
FfbLoginKennung LOGIN = FfbLoginKennung.of("22222301");
FfbPin PIN = FfbPin.of("91901");
```
Mit diesen Logindaten lässt sich ein Client erstellen. Dann führt man ein Login aus und holt sich die gewünschten Daten.
```java
FfbMobileClient mobileAgent = new FfbMobileClient(LOGIN, PIN);
mobileAgent.logon();
// Value-Objekte.
MyFfbResponse accountData = mobileAgent.fetchAccountData();
FfbPerformanceResponse performance = mobileAgent.getPerformance();
```
Da eine Loginkennung mehrere Depots enthalten kann (teilweise eine Depotnummer mehrfach), lassen sich die Gesamtbestände einfach mit der Utils-Klasse ermitteln:
```java
FfbDepotNummer depotNummer = FfbDepotNummer.of("222223"); // Login ohne -01.
BigDecimal gesamtBestand = FfbDepotUtils.getGesamtBestand(accountData, depotNummer);
```
Hinergrund zu den Depotnummern: Es kann mehrere Depots mit der selben Depotnummer geben: Einmal ein Standard-Depot, und einmal ein VL-Depot. Eine Loginkennung kann aber auch ggf. auf mehrere Depots zugreifen.


### Nutzungshinweise
(German) Das Protokoll ist von der FFB nicht offiziell freigegeben.
Zum Sniffen habe ich die App [Packet Capture](https://play.google.com/store/apps/details?id=app.greyshirts.sslcapture&hl=de)
für Android genutzt.


(English) The protocol is neither documented or officially released.
For sniffing I used packet capture for android, see link above.


### Protokollqualität (Quality of the protocol)
(German) Das Protokoll ist furchtbar schlecht designed. Es besitzt außer https (tls) keine Transportsicherheit.
Einige Felder fallen je nach anderen Feldern weg, oder sie nutzen oft den falschen Datentyp (gestringte Felder).
Das sind typische Fehler einer eigenen Protokollentwicklung.
Man hätte natürlich auch einfach [HBCI/FinTS](https://de.wikipedia.org/wiki/Financial_Transaction_Services) nutzen können, womit
man auch XML-Signaturen etc. hätte nutzen können.

Die Stringifizierung der Daten wird mittels Jackson Deserializer wieder rückgängig gemacht.
Derzeit werden Geldbeträge in BigDecimal gewandelt. Ein eigener Datentyp inkl. Währung wäre ebenfalls möglich.

(English) The protocol is awful. It does not have transport security other than relying on https (tls).
Most datatypes were stringified (booleans, floats, dates, etc.).
This is a problem when you design your own protocols. There is [FinTS](https://en.wikipedia.org/wiki/FinTS)
(famous among german banks) which uses XML Signatures etc.

Stringification is being reversed using Jackson Deserializers.
Money amounts are converted to BigDecimal instances. It would also be possible to convert to it's own datatype,
including currency.

### HTTPS protocol

The bank is not very trustworthy. It does offer the obsolete TLS 1, but no HTTP2.0 nor SPDY. On the other hand, triple
DES ciphers are allowed, they are probably vulnerable to Secure Client-Initiated Renegotiation, to BREACH and BEAST (DES-CBC3-SHA).

```bash
$ testssl.sh -p www.fidelity.de
--> Testing protocols (via sockets except TLS 1.2 and SPDY/NPN)

 SSLv2      not offered (OK)
 SSLv3      not offered (OK)
 TLS 1      offered
 TLS 1.1    not offered
 TLS 1.2    offered (OK)
 SPDY/NPN   not offered
```


## Protokoll
Das Protokoll basiert auf dem Mobile-Protokoll der FFB-App. Es verwendet Cookies für Session-Informationen und gibt bei bestimmten GET-Requests einfach JSON-Responses aus.


### Login
<table>
<thead><th>Parameter</th><th>Content</th></thead>
<tbody>
  <tr>
    <td>URL</td>
    <td>https://www.fidelity.de/de/mobile/MyFFB/account/userLogin.page</td>
  </tr>
    <tr>
    <td>Methode</td>
    <td>POST</td>
  </tr>
  <tr>
    <td>Request-Body</td>
    <td>login=*user*&password=*passwort*</td>
  </tr>
  <tr>
    <td>Header</td>
    <td><pre><code>Accept: application/json; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
Accept-Language: en-GB,en-US,en;q=0.8
Accept-Encoding: gzip,deflate
Accept-Charset: utf-8;q=0.7,*;q=0.3
X-Requested-With: ffb.depot.client
User-Agent: ffb.depot.client
Cache-Control: no-cache
Pragma: no-cache
Origin: file://</code></pre></td>
  </tr>
  <tr>
    <td>Response-Body</td>
    <td><pre><code>{
  "loggedIn": "true",
  "username": "Firstname Lastname",
  "firstname": "Firstname",
  "lastname": "Lastname"
  "usertype": "Customer",
  "ZustimmungNutzungsbedingungenFFS": "".
  "errormessage": ""
}</code></pre></td>
  </tr>
</tbody>
</table>


### Depotübersicht
<table>
<thead><th>Parameter</th><th>Content</th></thead>
<tbody>
  <tr>
    <td>URL</td>
    <td>https://www.fidelity.de/de/mobile/MyFFB/account/MyFFB.page</td>
  </tr>
  <tr>
    <td>Methode</td>
    <td>GET</td>
  </tr>
  <tr>
    <td>Header</td>
    <td><pre><code>Accept: application/json; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
Accept-Language: en-GB,en-US,en;q=0.8
Accept-Encoding: gzip,deflate
Accept-Charset: utf-8;q=0.7,*;q=0.3
X-Requested-With: ffb.depot.client
User-Agent: ffb.depot.client
Cache-Control: no-cache
Pragma: no-cache
Origin: file://</code></pre></td>
  </tr>
  <tr>
    <td>Response-Body</td>
    <td><pre><code>{
  "login": "true",
  "modelportfolio":
    false
    ,
  "letztesUpdate": "",
  "gesamtwert": "1.234,56",
  "depots": [
    {
      "depotname": "Standard-Depot",
      "depotnummer": "1234567890",
      "bestand": "1.234,56",
      "fondsbestaende"  : [
        {
          "wkn"           : "847512",
          "isin"            : "DE0008475120",
          "fondsname"         : "Allianz Informationstechnologie ",
          "fondswaehrung"       : "EUR",
          "bestandStueckzahl"     : "132,633",
          "bestandWertInFondswaehrung"  : "22.758,50",
          "bestandWertInEuro"     : "22.758,50",
          "ruecknahmepreis"     : "171,59",
          "preisDatum"        : "05.08.2016",
          "benchmarkName"       : "MSCI ACWI/INFORMATION TECH STRD USD"
        },
        {..}
      ]
    }
  ]
}</code></pre></td>
  </tr>
  <tr>
    <td>Anmerkdungen</td>
    <td>Hier wird bislang nur ein Ausschnitt der Daten gezeigt.</td>
</tbody>
</table>


### Depotperformance
<table>
<thead><th>Parameter</th><th>Content</th></thead>
<tbody>
  <tr>
    <td>URL</td>
    <td>https://www.fidelity.de/de/mobile/MyFFB/account/performance.page</td>
  </tr>
  <tr>
    <td>Methode</td>
    <td>GET</td>
  </tr>
  <tr>
    <td>Header</td>
    <td><pre><code>Accept: application/json; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
Accept-Language: en-GB,en-US,en;q=0.8
Accept-Encoding: gzip,deflate
Accept-Charset: utf-8;q=0.7,*;q=0.3
X-Requested-With: ffb.depot.client
User-Agent: ffb.depot.client
Cache-Control: no-cache
Pragma: no-cache
Origin: file://</code></pre></td>
  </tr>
  <tr>
    <td>Response-Body</td>
    <td><pre><code>{
  "login"                   : "true",
  "performanceGesamt"       : "22,67",

  "performanceDurchschnitt" : "7,93",

  "ersterZufluss"           : "22.01.1991",
  "errormessage"            : ""
}</code></pre></td>
  </tr>
<tr>
  <td>Anmerkungen</td>
  <td>Die Ausgabe erfolgt "pretty printed" und mit eigentlich unnötigen, sogar doppelten Zeilenumbruechen. Das Datum ist irrsinnigerweise nicht per ISO 8601, sondern im Deutschen Datumsformat angegeben. Ach die Prozentbeträge verwenden das international unübliche Komma als Dezimaltrennzeichen. Die Begriffe Zufluss, Durchschnitt und Gesamt sind Deutsche Begriffe, während die restlichen Begriffe englisch sind (login, performance, error, message).</td>
</tbody>
</table>


### Logout
<table>
<thead><th>Parameter</th><th>Content</th></thead>
<tbody>
  <tr>
    <td>URL</td>
    <td>https://www.fidelity.de/de/mobile/account/logout.page</td>
  </tr>
  <tr>
    <td>Method</td>
    <td>GET</td>
  </tr>
  <tr>
    <td>Header</td>
    <td><pre><code>Accept: application/json; q=0.01
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
Accept-Language: en-GB,en-US,en;q=0.8
Accept-Encoding: gzip,deflate
Accept-Charset: utf-8;q=0.7,*;q=0.3
X-Requested-With: ffb.depot.client
User-Agent: ffb.depot.client
Cache-Control: no-cache
Pragma: no-cache
Origin: file://</code></pre></td>
  </tr>
  <tr>
    <td>Response-Body</td>
    <td>none</td>
  </tr>
<tr>
  <td>Additional information</td>
  <td>Cookie should probably be deleted by hand.</td>
</tbody>
</table>

