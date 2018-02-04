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
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.Test;

public class GermanNumberDeserializerTest {

  private static final ObjectMapper OM = ObjectMapperProvider.getInstance();

  /**
   * Class under test.
   */
  private final GermanNumberToBigDecimalDeserializer deserializer = new GermanNumberToBigDecimalDeserializer();

  @Test
  public void legalNumber() throws IOException {
    final JsonParser dateString = OM.getFactory().createParser("\"211,4147\"");
    final DeserializationContext context = OM.getDeserializationContext();
    dateString.nextToken();

    final BigDecimal dec = deserializer.deserialize(dateString, context);

    assertThat(dec.intValue(), is(211));
    assertThat(dec.precision(), is(greaterThanOrEqualTo(4)));
    assertThat(dec.setScale(0, RoundingMode.HALF_UP).intValueExact(), is(211));
  }

  @Test(expected = IllegalArgumentException.class)
  public void notANumber() throws IOException {
    final JsonParser dateString = OM.getFactory().createParser("\"-2a1,473f41\"");
    final DeserializationContext context = OM.getDeserializationContext();
    dateString.nextToken();

    deserializer.deserialize(dateString, context);
  }
}
