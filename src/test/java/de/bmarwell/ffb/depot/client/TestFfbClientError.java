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
      throw new FfbClientError(new FfbClientError());
    } catch (final FfbClientError e) {
      Assert.assertNotNull("Thrown exception should not be null", e);
      /* the constructor should set the message to the cause's class name. */
      Assert.assertNotNull("Message should not be null.", e.getMessage());
      Assert.assertEquals("Message should be cause's class name.", FfbClientError.class.getName(), e.getMessage());

      Assert.assertNotNull("Cause was set, so it's not null.", e.getCause());
      Assert.assertEquals("Should have the cause.", FfbClientError.class, e.getCause().getClass());
    }
  }

}
