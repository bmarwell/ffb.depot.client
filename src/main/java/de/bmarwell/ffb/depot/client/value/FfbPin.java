package de.bmarwell.ffb.depot.client.value;

/**
 * Die Pin, die für das Login zusammen mit der {@link FfbLoginKennung} bei der FFB verwendet wird.
 *
 * <p>Sie wird intern im Speicher als CharArray zwischengespeichert, damit sie nicht ganz so offensichtlich im Speicher
 * liegt. Hier ist aber noch Optimierungspotential. Im Gegensatz zu z.B. Hibiscus selbst wird aber die Login nur so lange wie
 * notwendig im Speicher gehalten, und ist als CharArray nicht sofort als PIN ersichtlich.</p>
 */
public class FfbPin {

  /**
   * Die Pin wird als Char-Array gespeichert, damit sie wenigstens nicht komplett offensichtlich im Speicher liegt.
   */
  private char[] pin;

  public FfbPin(char[] pin) {
    this.pin = pin;
  }

  /**
   * Konstruktur für das Immutable-Objekt der Ffb-PIN.
   *
   * <p>Diese Methode ist gleichwertig zu {@link #of(char[])}, da einfach hier {@link String#toCharArray()} aufgerufen wird.
   *
   * @param pin
   *          die Pin als String.
   * @return eine FfbPin, immutable.
   */
  public static FfbPin of(String pin) {
    return new FfbPin(pin.toCharArray());
  }

  /**
   * Konstruktur für das Immutable-Objekt der Ffb-PIN.
   *
   * @param pin
   *          die Pin als char[].
   * @return eine FfbPin, immutable.
   */
  public static FfbPin of(char[] pin) {
    return new FfbPin(pin);
  }

  public String getPinAsString() {
    return new String(pin);
  }
}
