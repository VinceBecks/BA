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

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.core.util.EntityManagerProvider;

import de.openknowledge.playground.api.rest.security.application.IntegrationTestUtil;
import de.openknowledge.playground.api.rest.security.domain.customer.Customer;
import de.openknowledge.playground.api.rest.security.domain.customer.Gender;
import de.openknowledge.playground.api.rest.security.domain.customer.TestCustomers;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;

/**
 * Integration test class for the rest resource {@link CustomerResource}.
 */
public class CustomerResourceIT {

  private String baseURI = IntegrationTestUtil.getBaseURI();

  private String token;

  @Rule
  public EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");

  @Rule
  public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> entityManagerProvider.connection());

  @Before
  public void setUp() {
    AuthorizationResponse response = AuthzClient.create().authorization("@admin", "password").authorize();
    token = response.getToken();
  }

  @Test
  @DataSet(strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/customers-create-expected.yml", ignoreCols = "CUS_ID")
  public void createCustomer() {
    Customer defaultCustomer = TestCustomers.newDefaultCustomer();

    NewCustomer newCustomer = new NewCustomer();
    newCustomer.setFirstName(defaultCustomer.getFirstName());
    newCustomer.setLastName(defaultCustomer.getLastName());
    newCustomer.setEmailAddress(defaultCustomer.getEmailAddress());
    newCustomer.setGender(defaultCustomer.getGender());

    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .body(newCustomer)
        .when()
        .post(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.CREATED.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/Customer-schema.json"))
        .body("firstName", Matchers.equalTo("Max"))
        .body("lastName", Matchers.equalTo("Mustermann"))
        .body("emailAddress", Matchers.equalTo("max.mustermann@openknowledge.de"))
        .body("gender", Matchers.equalTo("MALE"));
  }

  @Test
  @DataSet(value = "datasets/customers-delete.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/customers-delete-expected.yml")
  public void deleteCustomer() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .when()
        .delete(getSingleItemUri(1L))
        .then()
        .statusCode(Status.NO_CONTENT.getStatusCode());
  }

  @Test
  @DataSet(value = "datasets/customers-delete.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/customers-delete.yml")
  public void deleteCustomerShouldFailForUnknownCustomer() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .when()
        .delete(getSingleItemUri(-1L))
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  @DataSet(value = "datasets/customers.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true)
  public void getCustomer() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .when()
        .get(getSingleItemUri(1L))
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.OK.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/Customer-schema.json"))
        .body("firstName", Matchers.equalTo("Max"))
        .body("lastName", Matchers.equalTo("Mustermann"))
        .body("emailAddress", Matchers.equalTo("max.mustermann@openknowledge.de"))
        .body("gender", Matchers.equalTo("MALE"));
  }

  @Test
  @DataSet(value = "datasets/customers.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true)
  public void getCustomerShouldFailForUnknownCustomer() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .when()
        .get(getSingleItemUri(-1L))
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  @DataSet(value = "datasets/customers.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true)
  public void getCustomers() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .when()
        .get(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.OK.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/Customers-schema.json"))
        .body("size()", Matchers.is(7));
  }

  @Test
  @DataSet(value = "datasets/customers-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/customers-update-expected.yml")
  public void updateCustomer() {
    Customer defaultCustomer = TestCustomers.newDefaultCustomer();

    ModifiedCustomer modifiedCustomer = new ModifiedCustomer();
    modifiedCustomer.setFirstName("Marta");
    modifiedCustomer.setLastName("Musterfrau");
    modifiedCustomer.setEmailAddress("marta.musterfrau@openknowledge.de");
    modifiedCustomer.setGender(Gender.FEMALE);

    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .body(modifiedCustomer)
        .when()
        .put(getSingleItemUri(1L))
        .then()
        .statusCode(Status.NO_CONTENT.getStatusCode());
  }

  @Test
  @DataSet(value = "datasets/customers-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/customers-update.yml")
  public void updateCustomerShouldFailForUnknownCustomer() {
    Customer defaultCustomer = TestCustomers.newDefaultCustomer();

    ModifiedCustomer modifiedCustomer = new ModifiedCustomer();
    modifiedCustomer.setFirstName(defaultCustomer.getFirstName());
    modifiedCustomer.setLastName(defaultCustomer.getLastName());
    modifiedCustomer.setEmailAddress(defaultCustomer.getEmailAddress());
    modifiedCustomer.setGender(defaultCustomer.getGender());

    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .body(modifiedCustomer)
        .when()
        .put(getSingleItemUri(-1L))
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  private URI getListUri() {
    return UriBuilder.fromUri(baseURI).path("api").path("customers").build();
  }

  private URI getSingleItemUri(final Long customerId) {
    return UriBuilder.fromUri(getListUri()).path("{id}").build(customerId);
  }
}
