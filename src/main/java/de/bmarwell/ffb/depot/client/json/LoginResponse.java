/*
 * (c) Copyright 2016 FFB Depot Client Developers.
 *
 * This file is part of FFB Depot Client.
 *
 * FFB Depot Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * FFB Depot Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FFB Depot Client.  If not, see <http://www.gnu.org/licenses/>.
 *
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
   * <p>Known values:<ul><li>Customer</li></ul></p>
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
