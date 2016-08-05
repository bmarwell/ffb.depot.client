# ffb.depot.client
Ein Java-Client für FFB-Depots. Features: Stammdaten und Depotbestände abholen.

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
</tbody>
</table>
