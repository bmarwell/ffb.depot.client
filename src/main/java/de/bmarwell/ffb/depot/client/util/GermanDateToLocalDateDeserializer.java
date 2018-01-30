package de.bmarwell.ffb.depot.client.util;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    super(TypeFactory.defaultInstance().constructType(LocalDate.class));
  }

  protected GermanDateToLocalDateDeserializer(Class<?> vc) {
    super(vc);
  }

  protected GermanDateToLocalDateDeserializer(JavaType valueType) {
    super(valueType);
  }


  @Override
  public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    final String germanDate = jsonParser.getText();

    requireNonNull(germanDate, () -> "Parameter germanDate in convertGermanDateToLocalDate.");
    requireNonNull(germanDate.matches("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}"), () -> "Format of GermanDate != dd.mm.yyyy");

    return LocalDate.parse(germanDate, GERMAN_DATE_FORMAT);
  }
}
