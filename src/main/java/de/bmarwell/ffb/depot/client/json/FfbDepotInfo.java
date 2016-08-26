package de.bmarwell.ffb.depot.client.json;

import de.bmarwell.ffb.depot.client.value.FfbDepotNummer;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.Collection;
import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
public abstract class FfbDepotInfo {

  @Value.Parameter
  public abstract String getDepotname();

  @Value.Parameter
  @SerializedName("depotnummer")
  protected abstract String getDepotNummerAsString();

  @Value.Derived
  public FfbDepotNummer getDepotNummer() {
    return FfbDepotNummer.of(getDepotNummerAsString());
  }

  @Value.Parameter
  @SerializedName("bestand")
  protected abstract String getBestandAsString();

  @Value.Derived
  public double getGesamtDepotBestand() {
    return Double.parseDouble(getBestandAsString().replace(".", "").replace(',', '.'));
  }

  @Value.Parameter
  public abstract List<FfbFondsbestand> getFondsbestaende();

  public static FfbDepotInfo of(
      String depotname,
      String depotnummer,
      String bestand,
      Collection<FfbFondsbestand> bestaende) {
    return ImmutableFfbDepotInfo.of(depotname, depotnummer, bestand, ImmutableList.<FfbFondsbestand>copyOf(bestaende));
  }

  public static FfbDepotInfo of(String depotname, FfbDepotNummer depotnummer, double bestand,
      Collection<FfbFondsbestand> bestaende) {
    return FfbDepotInfo.of(
        depotname,
        depotnummer.getDepotNummer(),
        Double.toString(bestand).replace('.', ','),
        bestaende);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("depotname", getDepotname())
        .add("depotnummer", getDepotNummer())
        .add("bestand", getGesamtDepotBestand())
        .add("fondsbestaende", getFondsbestaende())
        .toString();
  }
}
