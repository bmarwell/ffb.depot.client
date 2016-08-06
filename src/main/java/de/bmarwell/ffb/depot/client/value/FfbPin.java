package de.bmarwell.ffb.depot.client.value;

public class FfbPin {

  /**
   * Die Pin wird als Char-Array gespeichert, damit sie wenigstens nicht komplett offensichtlich im Speicher liegt.
   */
  private char[] pin;

  public FfbPin(char[] pin) {
    this.pin = pin;
  }

  public static FfbPin of(String pin) {
    return new FfbPin(pin.toCharArray());
  }

  public static FfbPin of(char[] pin) {
    return new FfbPin(pin);
  }

  public String getPinAsString() {
    return new String(pin);
  }
}
