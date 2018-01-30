package de.bmarwell.ffb.depot.client.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class GermanNumberToBigDecimalDeserializer extends StdDeserializer<BigDecimal> {

  public GermanNumberToBigDecimalDeserializer() {
    super(TypeFactory.defaultInstance().constructType(BigDecimal.class));
  }

  protected GermanNumberToBigDecimalDeserializer(Class<?> vc) {
    super(vc);
  }

  protected GermanNumberToBigDecimalDeserializer(JavaType valueType) {
    super(valueType);
  }

  @Override
  public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    final String germanNumStrin = jsonParser.getText();

    if (null == germanNumStrin || germanNumStrin.isEmpty()) {
      return new BigDecimal(0);
    }

    final String usString = germanNumStrin.replace(".", "").replace(',', '.');

    return new BigDecimal(usString);
  }
}
