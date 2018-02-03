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

  public static final ObjectMapper OM = ObjectMapperProvider.getInstance();
  GermanNumberToBigDecimalDeserializer deserializer = new GermanNumberToBigDecimalDeserializer();

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

    final BigDecimal dec = deserializer.deserialize(dateString, context);
  }
}
