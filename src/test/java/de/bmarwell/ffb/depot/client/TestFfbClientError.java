package de.bmarwell.ffb.depot.client;

import de.bmarwell.ffb.depot.client.err.FfbClientError;

import org.junit.Assert;
import org.junit.Test;

public class TestFfbClientError {

  @Test
  public void testClientError() {
    try {
      throw new FfbClientError();
    } catch (final FfbClientError e) {
      Assert.assertNotNull("Exception should not be null.", e);
    }
  }

  @Test
  public void testClientErrorMessage() {
    try {
      throw new FfbClientError("message");
    } catch (final FfbClientError e) {
      Assert.assertNotNull("Thrown exception should not be null", e);
      Assert.assertNotNull("Message should not be null.", e.getMessage());
      Assert.assertNull("Cause was not set.", e.getCause());
    }
  }

  @Test
  public void testClientErrorThrowable() {
    try {
      throw new FfbClientError(new RuntimeException());
    } catch (final FfbClientError e) {
      Assert.assertNotNull("Thrown exception should not be null", e);
      /* the constructor should set the message to the cause's class name. */
      Assert.assertNotNull("Message should not be null.", e.getMessage());
      Assert.assertEquals("Message should be cause's class name.", RuntimeException.class.getName(), e.getMessage());

      Assert.assertNotNull("Cause was set, so it's not null.", e.getCause());
      Assert.assertEquals("Should have the cause.", RuntimeException.class, e.getCause().getClass());
    }
  }

}
