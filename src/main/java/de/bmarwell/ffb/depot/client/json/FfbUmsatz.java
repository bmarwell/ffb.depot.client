package de.bmarwell.ffb.depot.client.json;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
public abstract class FfbUmsatz {
  @SerializedName("depotnummer")
  protected abstract String getDepotnummer();

  @SerializedName("isin")
  protected abstract String getIsin();

  @SerializedName("wkn")
  protected abstract String getWkn();

  @SerializedName("fondsname")
  protected abstract String getFondsname();

  @SerializedName("fondsgesellschaft")
  protected abstract String getFondsgesellschaft();

  @SerializedName("transaktionArt")
  protected abstract String getTransaktionArt();

  @SerializedName("buchungDatum")
  protected abstract String getBuchungDatum();

  @SerializedName("abrechnungDatum")
  protected abstract String getAbrechnungDatum();

  @SerializedName("abrechnungAnteile")
  protected abstract String getAbrechnungAnteile();

  @SerializedName("abrechnungpreis")
  protected abstract String getAbrechnungpreis();

  @SerializedName("fondswaehrung")
  protected abstract String getFondswaehrung();

  @SerializedName("ausgabeaufschlagInProzent")
  protected abstract String getAusgabeaufschlagInProzent();

  @SerializedName("rabattProzent")
  protected abstract String getRabattProzent();

  @SerializedName("abrechnungBetragInFondswaehrung")
  protected abstract String getAbrechnungBetragInFondswaehrung();

  @SerializedName("abrechnungBetragInEuro")
  protected abstract String getAbrechnungBetragInEuro();

}
