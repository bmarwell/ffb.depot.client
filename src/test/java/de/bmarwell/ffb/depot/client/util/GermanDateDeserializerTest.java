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
