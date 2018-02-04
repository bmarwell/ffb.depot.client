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
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableFfbUmsatz.class)
public interface FfbUmsatz {

  @JsonProperty("depotnummer")
  String getDepotnummer();

  @JsonProperty("isin")
  String getIsin();

  @JsonProperty("wkn")
  String getWkn();

  @JsonProperty("fondsname")
  String getFondsname();

  @JsonProperty("fondsgesellschaft")
  String getFondsgesellschaft();

  @JsonProperty("transaktionArt")
  String getTransaktionArt();

  @JsonProperty("buchungDatum")
  String getBuchungDatum();

  @JsonProperty("abrechnungDatum")
  String getAbrechnungDatum();

  @JsonProperty("abrechnungAnteile")
  String getAbrechnungAnteile();

  @JsonProperty("abrechnungpreis")
  String getAbrechnungpreis();

  @JsonProperty("fondswaehrung")
  String getFondswaehrung();

  @JsonProperty("ausgabeaufschlagInProzent")
  String getAusgabeaufschlagInProzent();

  @JsonProperty("rabattProzent")
  String getRabattProzent();

  @JsonProperty("abrechnungBetragInFondswaehrung")
  String getAbrechnungBetragInFondswaehrung();

  @JsonProperty("abrechnungBetragInEuro")
  String getAbrechnungBetragInEuro();

}
