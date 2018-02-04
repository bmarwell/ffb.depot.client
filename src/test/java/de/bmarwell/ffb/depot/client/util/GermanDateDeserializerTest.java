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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import org.junit.Test;

public class GermanDateDeserializerTest {

  private static final ObjectMapper OM = ObjectMapperProvider.getInstance();

  /**
   * Class under test.
   */
  private final GermanDateToLocalDateDeserializer deserializer = new GermanDateToLocalDateDeserializer();

  @Test
  public void legalDate() throws IOException {
    final JsonParser dateString = OM.getFactory().createParser("\"17.01.1991\"");
    final DeserializationContext context = OM.getDeserializationContext();
    dateString.nextToken();

    final LocalDate date = deserializer.deserialize(dateString, context);

    assertThat(date.getYear(), is(1991));
    assertThat(date.getMonth(), is(Month.JANUARY));
    assertThat(date.getDayOfMonth(), is(17));
  }

  @Test(expected = IllegalArgumentException.class)
  public void notADate() throws IOException {
    final JsonParser dateString = OM.getFactory().createParser("\"-1.14.24291\"");
    final DeserializationContext context = OM.getDeserializationContext();
    dateString.nextToken();

    final LocalDate date = deserializer.deserialize(dateString, context);
  }
}
