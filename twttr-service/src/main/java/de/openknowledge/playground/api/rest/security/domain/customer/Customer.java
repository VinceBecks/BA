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
package de.openknowledge.playground.api.rest.security.domain.customer;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * An entity that represents a customer;
 */
@Entity
@Table(name = "TAB_CUSTOMER")
public class Customer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
  @TableGenerator(name = "TABLE_GEN", table = "SEQUENCE_TABLE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUS_SEQ", initialValue = 1000, allocationSize = 1)
  @Column(name = "CUS_ID", nullable = false)
  private Long id;

  @Column(name = "CUS_FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "CUS_LAST_NAME", nullable = false)
  private String lastName;

  @Column(name = "CUS_EMAIL", nullable = false)
  private String emailAddress;

  @Enumerated(EnumType.STRING)
  @Column(name = "CUS_GENDER", nullable = false)
  private Gender gender;

  public Long getId() {
    return id;
  }

  protected void setId(final Long id) {
    this.id = id;
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
  public int hashCode() {
    if (id == null) {
      return new Object().hashCode();
    } else {
      return id.hashCode();
    }
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (object == null
      || !(object.getClass().isAssignableFrom(getClass()) && getClass().isAssignableFrom(object.getClass()))) {
      return false;
    }
    Customer customer = (Customer)object;
    return getId() != null && getId().equals(customer.getId());
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "#" + id;
  }
}
