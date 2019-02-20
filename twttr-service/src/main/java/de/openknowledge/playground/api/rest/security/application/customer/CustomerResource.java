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

import static de.openknowledge.playground.api.rest.security.infrastructure.security.Roles.MODERATOR;
import static de.openknowledge.playground.api.rest.security.infrastructure.security.Roles.USER;

import de.openknowledge.playground.api.rest.security.domain.customer.Customer;
import de.openknowledge.playground.api.rest.security.domain.customer.CustomerNotFoundException;
import de.openknowledge.playground.api.rest.security.domain.customer.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * A resource that provides access to the {@link Customer} entity.
 */
@Path("customers")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class CustomerResource {

  private static final Logger LOG = LoggerFactory.getLogger(CustomerResource.class);

  @Inject
  private CustomerRepository repository;

  @POST
  @Transactional
  @RolesAllowed({MODERATOR})
  public Response createCustomer(final NewCustomer newCustomer) {
    LOG.info("Create customer {}", newCustomer);

    Customer customer = new Customer();
    customer.setFirstName(newCustomer.getFirstName());
    customer.setLastName(newCustomer.getLastName());
    customer.setEmailAddress(newCustomer.getEmailAddress());
    customer.setGender(newCustomer.getGender());

    repository.create(customer);

    CustomerResourceType createdCustomer = new CustomerResourceType(customer);

    LOG.info("Customer created {}", createdCustomer);

    return Response.status(Status.CREATED).entity(createdCustomer).build();
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  @RolesAllowed({MODERATOR})
  public Response deleteCustomer(@PathParam("id") final Long customerId) {
    LOG.info("Delete customer with id {}", customerId);

    try {
      Customer customer = repository.find(customerId);
      repository.delete(customer);

      LOG.info("Customer deleted");

      return Response.status(Status.NO_CONTENT).build();
    } catch (CustomerNotFoundException e) {
      LOG.warn("Customer with id {} not found", customerId);
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @GET
  @Path("/{id}")
  @RolesAllowed({MODERATOR, USER})
  public Response getCustomer(@PathParam("id") final Long customerId) {
    LOG.info("Find customer with id {}", customerId);

    try {
      Customer customer = repository.find(customerId);
      CustomerResourceType foundCustomer = new CustomerResourceType(customer);

      LOG.info("Found customer {}", foundCustomer);

      return Response.status(Status.OK).entity(foundCustomer).build();
    } catch (CustomerNotFoundException e) {
      LOG.warn("Customer with id {} not found", customerId);
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @GET
  @RolesAllowed({MODERATOR})
  public Response getCustomers() {
    LOG.info("Find all customers");

    List<CustomerResourceType> customers = repository.findAll()
        .stream()
        .map(CustomerResourceType::new)
        .collect(Collectors.toList());

    LOG.info("Found {} customers", customers.size());

    return Response.status(Status.OK)
        .entity(new GenericEntity<List<CustomerResourceType>>(customers) {
        }).build();
  }

  @PUT
  @Path("/{id}")
  @Transactional
  @RolesAllowed({MODERATOR, USER})
  public Response updateCustomer(@PathParam("id") final Long customerId, final ModifiedCustomer modifiedCustomer) {
    LOG.info("Update customer with id {}", customerId);

    try {
      Customer foundCustomer = repository.find(customerId);

      foundCustomer.setFirstName(modifiedCustomer.getFirstName());
      foundCustomer.setLastName(modifiedCustomer.getLastName());
      foundCustomer.setEmailAddress(modifiedCustomer.getEmailAddress());
      foundCustomer.setGender(modifiedCustomer.getGender());

      Customer updatedCustomer = repository.update(foundCustomer);

      LOG.info("Customer updated {}", updatedCustomer);

      return Response.status(Status.NO_CONTENT).build();
    } catch (CustomerNotFoundException e) {
      LOG.warn("Customer with id {} not found", customerId);
      return Response.status(Status.NOT_FOUND).build();
    }
  }
}
