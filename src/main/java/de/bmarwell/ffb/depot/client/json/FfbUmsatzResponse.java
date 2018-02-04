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

package de.bmarwell.ffb.depot.client.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableFfbUmsatzResponse.class)
public interface FfbUmsatzResponse {

  @JsonProperty("login")
  String getlogin();

  @JsonProperty("error")
  String getError();

  @JsonProperty("anzahlUmsaetze")
  String getAnzahlUmsaetze();

  /**
   * <p>Example: &quot;/de/fonds/quick-factsheet-overlay.page?&quot;</p>
   *
   * @return a path fragment.
   */
  @JsonProperty("urlFactsheetOverlay")
  String getUrlFactsheetOverlay();

  String getHash();

  @JsonProperty("umsaetze")
  List<FfbUmsatz> getUmsaetze();
}
