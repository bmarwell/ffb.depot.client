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

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.slf4j.Logger;

public class GermanNumberToBigDecimalDeserializer extends StdDeserializer<BigDecimal> {

  /**
   * Logger.
   */
  private static final Logger LOG = getLogger(GermanNumberToBigDecimalDeserializer.class);

  public GermanNumberToBigDecimalDeserializer() {
    this(TypeFactory.defaultInstance().constructType(BigDecimal.class));
  }

  protected GermanNumberToBigDecimalDeserializer(final Class<?> vc) {
    super(vc);
  }

  protected GermanNumberToBigDecimalDeserializer(final JavaType valueType) {
    super(valueType);
  }

  @Override
  public BigDecimal deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
      throws IOException {
    final String germanNumStrin = jsonParser.getText();

    if (null == germanNumStrin || germanNumStrin.isEmpty()) {
      LOG.warn("String was null or empty for number!");
      return new BigDecimal(0);
    }

    final String usString = germanNumStrin.replace(".", "").replace(',', '.');

    /*
     * It is recommended to use the string constructor anyway.
     */
    return new BigDecimal(usString).setScale(4, RoundingMode.HALF_UP);
  }
}
