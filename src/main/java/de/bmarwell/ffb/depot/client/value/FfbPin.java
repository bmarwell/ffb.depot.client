/*
 * (c) Copyright 2016 FFB Depot Client Developers.
 *
 * This file is part of FFB Depot Client.
 *
 * FFB Depot Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * FFB Depot Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FFB Depot Client.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.bmarwell.ffb.depot.client.value;

import java.util.Arrays;

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
    // Should store a copy, so the pin won't change while working with it.
    this.pin = Arrays.copyOf(pin, pin.length);
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
