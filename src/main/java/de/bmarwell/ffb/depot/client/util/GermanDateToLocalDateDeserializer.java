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

package de.bmarwell.ffb.depot.client.util;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GermanDateToLocalDateDeserializer extends StdDeserializer<LocalDate> {

  /**
   * German date: <code>dd.MM.YYYY</code>.
   *
   * <p>I think it is kind of stupid of the FFB not to use ISO date and then convert it. This date
   * holds no locale information or whatsoever.<br><br> See also: <a href="https://xkcd.com/1179/">XKCD
   * ISO 8601</a></p>
   */
  public static final DateTimeFormatter GERMAN_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  public GermanDateToLocalDateDeserializer() {
    this(TypeFactory.defaultInstance().constructType(LocalDate.class));
  }

  protected GermanDateToLocalDateDeserializer(final Class<?> vc) {
    super(vc);
  }

  protected GermanDateToLocalDateDeserializer(final JavaType valueType) {
    super(valueType);
  }


  @Override
  public LocalDate deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
      throws IOException {
    final String germanDate = jsonParser.getText();
    requireNonNull(germanDate, "Parameter germanDate in convertGermanDateToLocalDate.");

    if (!germanDate.matches("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}")) {
      throw new IllegalArgumentException("Format of GermanDate != dd.mm.yyyy");
    }

    return LocalDate.parse(germanDate, GERMAN_DATE_FORMAT);
  }
}
