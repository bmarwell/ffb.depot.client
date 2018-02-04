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

package de.bmarwell.ffb.depot.client.json;

import de.bmarwell.ffb.depot.client.json.ImmutableLoginResponse.Builder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * Information which is sent by the server on successful login.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableLoginResponse.class)
public interface LoginResponse {

  static Builder builder() {
    return ImmutableLoginResponse.builder();
  }

  @JsonProperty("loggedIn")
  boolean isLoggedIn();

  /**
   * The name of the user.
   *
   * @return the username.
   */
  @JsonProperty("username")
  @Value.Default
  default String getUsername() {
    return "";
  }

  /**
   * The customers first name.
   *
   * @return the first name.
   */
  @Value.Default
  default String getFirstname() {
    return "";
  }

  /**
   * The customers last name.
   *
   * @return the last name.
   */
  @Value.Default
  default String getLastname() {
    return "";
  }

  /**
   * The type of the user.
   *
   * <p>Known values:</p>
   *
   * <ul>
   *   <li>Customer</li>
   * </ul>
   *
   * @return the user type.
   */
  @Value.Default
  default String getUsertype() {
    return "NOT_LOGGED_IN";
  }

  /**
   * Error message set by FFB.
   *
   * @return the error message.
   */
  Optional<String> getErrormessage();

  @JsonProperty("ZustimmungNutzungsbedingungenFFS")
  boolean getAgbAgreed();
}
