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
