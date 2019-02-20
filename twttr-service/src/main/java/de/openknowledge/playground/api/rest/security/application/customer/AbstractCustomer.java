/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package de.openknowledge.playground.api.rest.security.application.customer;

import de.openknowledge.playground.api.rest.security.domain.customer.Gender;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Abstract customer.
 */
public class AbstractCustomer implements Serializable {

  @NotNull
  @Size(min = 1, max = 30)
  @Pattern(regexp = "[ a-zA-ZÄäÖöÜüß\\´\\`\\&\\-]+")
  private String firstName;

  @NotNull
  @Size(min = 1, max = 30)
  @Pattern(regexp = "[ a-zA-ZÄäÖöÜüß\\´\\`\\&\\-]+")
  private String lastName;

  @NotNull
  @Size(min = 1, max = 50)
  private String emailAddress;

  @NotNull
  private Gender gender;

  public AbstractCustomer() {
    super();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(final Gender gender) {
    this.gender = gender;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AbstractCustomer that = (AbstractCustomer)o;

    if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) {
      return false;
    }
    if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) {
      return false;
    }
    if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) {
      return false;
    }
    return gender == that.gender;

  }

  @Override
  public int hashCode() {
    int result = firstName != null ? firstName.hashCode() : 0;
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
    result = 31 * result + (gender != null ? gender.hashCode() : 0);
    return result;
  }
}
