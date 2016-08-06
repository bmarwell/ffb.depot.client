# ffb.depot.client
Ein Java-Client für FFB-Depots. Features: Stammdaten und Depotbestände abholen. Es war ursprünglich eine Scraping-Library für die FFB, die aus dem Projekt [Hibiscus FFB-Depot](https://github.com/bmhm/hibiscus.ffb.depot) entstanden ist. Inzwischen verwendet sie das mobile JSON-Interface von Fidelity.

(English) This used to be a FFB scraping library written in Java. Since there is now a mobile HTTP-JSON-Interface, the project switched to this kind of implementation. It is used in the Hibiscus FFB Depot-Project (see link above).


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
  "modelportfolio": "false",
  "letztesUpdate": "",
  "gesamtwert": "1.234,56",
  "depots": [
    {
      "depotname": "Standard-Depot",
      "depotnummer": "1234567890",
      "bestand": "1.234,56"
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
