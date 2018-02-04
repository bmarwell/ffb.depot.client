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

package de.bmarwell.ffb.depot.client.value;

import org.immutables.value.Value;

/**
 * Die Pin, die für das Login zusammen mit der {@link FfbLoginKennung} bei der FFB verwendet wird.
 *
 * <p>Sie wird intern im Speicher als CharArray zwischengespeichert, damit sie nicht ganz so offensichtlich im Speicher
 * liegt. Hier ist aber noch Optimierungspotential. Im Gegensatz zu z.B. Hibiscus selbst wird aber die Login nur so lange wie
 * notwendig im Speicher gehalten, und ist als CharArray nicht sofort als PIN ersichtlich.</p>
 */
@Value.Immutable
public abstract class FfbPin {

  /**
   * Konstruktur für das Immutable-Objekt der Ffb-PIN.
   *
   * <p>Diese Methode ist gleichwertig zu {@link #of(char[])}, da einfach hier {@link String#toCharArray()} aufgerufen wird.
   *
   * @param pin
   *          die Pin als String.
   * @return eine FfbPin, immutable.
   */
  public static FfbPin of(final String pin) {
    return ImmutableFfbPin.of(pin.toCharArray());
  }

  /**
   * Konstruktur für das Immutable-Objekt der Ffb-PIN.
   *
   * @param pin
   *          die Pin als char[].
   * @return eine FfbPin, immutable.
   */
  public static FfbPin of(final char[] pin) {
    return ImmutableFfbPin.of(pin);
  }

  @Value.Parameter
  public abstract char[] getPin();

  public String getPinAsString() {
    return new String(getPin());
  }
}
