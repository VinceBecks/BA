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

import de.openknowledge.playground.api.rest.security.domain.customer.Customer;
import de.openknowledge.playground.api.rest.security.domain.customer.Gender;

import java.io.Serializable;

/**
 * An DTO that represents a {@link Customer}.
 */
public class CustomerResourceType implements Serializable {

  private Long id;

  private String firstName;

  private String lastName;

  private String emailAddress;

  private Gender gender;

  public CustomerResourceType() {
    super();
  }

  public CustomerResourceType(final Customer customer) {
    this();
    this.id = customer.getId();
    this.firstName = customer.getFirstName();
    this.lastName = customer.getLastName();
    this.emailAddress = customer.getEmailAddress();
    this.gender = customer.getGender();
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public Gender getGender() {
    return gender;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CustomerResourceType that = (CustomerResourceType)o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
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
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
    result = 31 * result + (gender != null ? gender.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CustomerResourceType{" +
      "id=" + id +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      ", emailAddress='" + emailAddress + '\'' +
      ", gender='" + gender + '\'' +
      '}';
  }
}